package de.htwg.se.bettler
package aview
package tui

import scala.util.{Failure, Success, Try}
import scala.io.StdIn.readLine
import de.htwg.se.bettler.controller.*
import util.Observer
import model.stateComponent.GameStateContext
import model.cardComponent.cardBaseImpl.Card
import model.cardComponent.cardBaseImpl.Cards
import model.cardComponent.*
import model.stateComponent.stateBaseImpl.*

import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.javadsl.model.{StatusCodes, Uri}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.*
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.http.scaladsl.unmarshalling.Unmarshal
import scala.swing.Reactor
import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.javadsl.model.{StatusCodes, Uri}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.*
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, Materializer, SystemMaterializer}

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.*
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.*
import akka.http.scaladsl.model.headers.*
import akka.stream.Materializer
import concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.sys.process.*
import java.nio.charset.StandardCharsets
import scala.swing.Publisher
import scala.swing.event.Event
import de.htwg.se.bettler.aview.tui.*


class TUIRest() extends Observer:
  var exit = false
  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: Materializer = SystemMaterializer(system).materializer

  val serverUri = s"http://0.0.0.0:8080/controller/"
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


  def run =
    println("Willkommen zu Bettler. Tippe 'start pvp oder start pve' ein um das Spiel zu starten.")
    println("Mit 'exit' kannst du jederzeit das Spiel beenden.")
    TUI()

  def update =
    println("upfa")

  def TUI(): Unit = {
    val input = readLine
    input match {
      case "start pvp" =>
        val result = getRequest("newgame?p1=pvp")
        waitRefreshGame(result)
      case "start pve" =>
        val result = getRequest("newgame?p1=pve")
        waitRefreshGame(result)
      case "exit" => println("test")
      case "skip" =>
        val result = getRequest("skip")
        waitRefreshGame(result)
      case "quicksave" => println("test")
      case "restore" => println("test")
      case "undo" =>
        val result = getRequest("undo")
        waitRefreshGame(result)
      case "redo" =>
        val result = getRequest("redo")
        waitRefreshGame(result)
      case "next" => println("test")
      case "save" => println("test")
      case "load" => println("test")
      case _ =>
        if (!input.isInstanceOf[String]) {
          return
        } else {
          if (input.startsWith("play")) {
            if (!GameStateContext.getState().isInstanceOf[PlayerTurnState]) {
              println("Start a game first.")
            } else {
               var x = input.replace("play ", "_")
               x = x.replace(" ","_")
              val result = getRequest("play?p1=" + x )
              waitRefreshGame(result)
            }
          } else {
            println("Unknown command.")
          }
        }
    }
    if (!exit) {
      TUI()
    }
  }
