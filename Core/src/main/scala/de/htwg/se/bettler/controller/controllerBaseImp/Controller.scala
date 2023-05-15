package de.htwg.se.bettler
package controller
package controllerBaseImp

import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.javadsl.model.{StatusCodes, Uri}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.*
import akka.http.scaladsl.model.headers.*
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, Materializer, SystemMaterializer}
import com.google.inject.name.Names
import com.google.inject.{Guice, Inject}
import de.htwg.se.bettler.fileIOComponent.fileIOJson.FileIO
import de.htwg.se.bettler.model.*
import de.htwg.se.bettler.model.cardComponent.*
import de.htwg.se.bettler.model.gameComponent.Game
import de.htwg.se.bettler.util.*
import net.codingwell.scalaguice.InjectorExtensions.*


import java.nio.charset.StandardCharsets
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.swing.{Publisher, Reactor}
import scala.swing.event.Event
import scala.sys.process.*

import fileIOComponent.database._
import scala.util.Try



case class Controller(var game : Option[Game]) extends ControllerInterface:


    val undomanager = util.UndoManager()
    val fileIO = new FileIO
    val dao = new SlickDAO
    implicit val system: ActorSystem = ActorSystem()
    implicit val mat: Materializer = SystemMaterializer(system).materializer

    val serverUri = s"http://localhost:8089/persistence/"
    private val http = Http()

    def getRequest(path: String): Future[HttpResponse] = {
        val request = HttpRequest(
            method = HttpMethods.GET,
            uri = serverUri + path
        )
        http.singleRequest(request)
    }

    def waitRefreshGame(resulti: Future[HttpResponse]): Unit = {
        val res = resulti.flatMap { response =>
            response.status match {
                case StatusCodes.OK =>
                    Unmarshal(response.entity).to[String].map { string =>
                        println(string.toString)
                    }
                case _ =>
                    Future.failed(new RuntimeException(s"HTTP request failed with status ${response.status} and entity ${response.entity}"))
            }
        }
        Await.result(res, 10.seconds)

    }
    override def toString = 
        game match
            case Some(g) => g.toString
            case None => "Currently no game running."

    def returnGame = game

    def doAndNotify(p : (CardsInterface) => Option[Game], cards : CardsInterface) : Unit =
        undomanager.doStep(PlayCommand(this))
        val newGame = p(cards)
        newGame match
            case Some(newGame) => 
                game = Some(newGame)
            case None => 
                game = newGame
        notifyObservers
        publish(new GameChanged())

    def doAndNotify(p : (String) => Option[Game], kind : String) : Unit =
        val newGame = p(kind)
        newGame match
            case Some(newGame) => game = Some(newGame)
            case None => game = None
        notifyObservers
        publish(new GameChanged())

    def doAndNotify(p : () => Option[Game]) : Unit =
        undomanager.doStep(PlayCommand(this))
        val newGame = p()
        newGame match
            case Some(newGame) => 
                game = Some(newGame)
            case None => 
                game = newGame
        notifyObservers
        publish(new GameChanged())

    def restore : Unit =
        if !stack.isEmpty then
            game match
                case Some(g) => game = Some(g.restore(this.getMemento()))
                case None => game = None
            notifyObservers
            publish(new GameChanged())

    def play(cards : CardsInterface) : Option[Game] =
        game match
            case Some(newGame) => Some(newGame.play(cards))
            case None => None
            
    def skip() : Option[Game] =
        game match
            case Some(newGame) => Some(newGame.skip())
            case None => None

    def newGame(kind : String) : Option[Game] =
        kind match
            case "pve" => Some(Game(kind))
            case "pvp" => Some(Game(kind))
            case _ => None

    def nextRound() : Option[Game] =
        game match
            case Some(newGame) => Some(newGame.nextRound)
            case None => None

    def addMemento() : Unit = 
        game match
            case Some(g) => stack.push(g.save())
            case None => return

    def getMemento() : Memento =
        stack.pop()

    def undo : Unit = 
        undomanager.undoStep
        notifyObservers
        publish(new GameChanged())

    def redo : Unit =
        undomanager.redoStep
        notifyObservers
        publish(new GameChanged())

    def exit : Unit =
        publish(new CloseEvent)

    def save : Unit =
        game match
            case Some(g) => dao.save(g)
            case None => return
    def load : Unit =
        game = Some(dao.load())
        notifyObservers
        publish(new GameChanged())