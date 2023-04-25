package de.htwg.se.bettler
package controller

import com.google.inject.name.Names
import com.google.inject.{Guice, Inject}
import model.gameComponent.*
import util.*
import de.htwg.se.bettler.fileIOComponent.fileIOJson.FileIO

import scala.swing.Publisher
import scala.swing.event.Event
import model.*
import fileIOComponent.*
import net.codingwell.scalaguice.InjectorExtensions.*
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{entity, *}
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.stream.ActorMaterializer
import controller.ControllerInterface

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}
import akka.protobufv3.internal.compiler.PluginProtos.CodeGeneratorResponse.File
import de.htwg.se.bettler.fileIOComponent.fileIOJson.FileIO
import de.htwg.se.bettler.model.cardComponent.{CardInterface, CardsInterface}
import de.htwg.se.bettler.model.cardComponent.cardBaseImpl.{Card, Cards}
import play.api.libs.json.*

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{entity, *}
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.stream.ActorMaterializer
import controller.ControllerInterface
import model.gameComponent._

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}
import akka.protobufv3.internal.compiler.PluginProtos.CodeGeneratorResponse.File
import play.api.libs.json.*


class ControllerAPI(controller: ControllerInterface):
  

  implicit def start(): Unit = {
    val binding = Http().newServerAt("localhost", RestUIPort).bind(route)

    binding.onComplete {
      case Success(binding) => {
        println(s"Successfully started")
      }
      case Failure(exception) => {
        println(s"Start Failed: ${exception.getMessage}")
      }
    }
  }
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  def toCards(cards: String): CardsInterface = {
    val s = cards.split(" ")
    var cardsSelected = Set.empty[CardInterface]
    for (i <- 1 until s.size) {
      Card(s(i)) match {
        case Success(c) => cardsSelected += c
        case Failure(f) => println(f.getMessage)
      }
    }
    Cards(cardsSelected)
  }


  val fileIO = new FileIO()
  val RestUIPort = 8080
  val routes: String =
    """
        """.stripMargin

  val route: Route =
    concat(
      pathSingleSlash {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, routes))
      },
      get {
        path("controller" / "get") {
          fileIO.save(controller.returnGame.get)
          print("get completed")
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, fileIO.gametoJson(controller.returnGame.get).toString()))
        }
      },
      get {
        path("controller" / "test") {
          fileIO.save(controller.returnGame.get)
          print("get completed")
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, (controller.returnGame.get.getBoard()).toString()))
        }
      },
      get {
        path("controller" / "redo") {
          controller.redo
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, fileIO.gametoJson(controller.returnGame.get).toString()))
        }
      },
      get {
        controller.undo
        path("controller" / "undo") {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, fileIO.gametoJson(controller.returnGame.get).toString()))
        }
      },
      get {
        path("controller" / "load") {
          controller.load
          complete(HttpEntity(ContentTypes.`application/json`, fileIO.gametoJson(controller.returnGame.get).toString()))
        }
      },
      post {
        path("controller" / "save") {
          controller.save
          complete(HttpEntity(ContentTypes.`application/json`, fileIO.gametoJson(controller.returnGame.get).toString()))
        }
      },
      post {
        path("controller" / "newgame") {
          fileIO.save(controller.returnGame.get)
          parameter("kind") { (kind) =>
            controller.newGame(kind)
            complete(HttpEntity(ContentTypes.`application/json`, fileIO.gametoJson(controller.returnGame.get).toString()))
          }
        }
      },
      post {
        path("controller" / "play") {
          parameter("cards") { (cards) =>
            print(cards+"these are the cards u want to play ")
            controller.play(toCards(cards))
            print("controller trying to play")
            complete(HttpEntity(ContentTypes.`application/json`, fileIO.gametoJson(controller.returnGame.get).toString()))
          }
        }
      },
      post {
        path("controller" / "skip") {
          controller.skip()
          complete(HttpEntity(ContentTypes.`application/json`, fileIO.gametoJson(controller.returnGame.get).toString()))
        }

      },

    )






