package de.htwg.se.bettler
package controller
package controllerBaseImp

import model.stateComponent.GameStateContext
import model.cardComponent.cardBaseImpl.Card
import model.cardComponent.cardBaseImpl.Cards
import model.cardComponent.*
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.protobufv3.internal.compiler.PluginProtos.CodeGeneratorResponse.File
import akka.stream.ActorMaterializer
import play.api.libs.json.*
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.http.scaladsl.unmarshalling.Unmarshal
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import akka.http.scaladsl.model.HttpRequest

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}
import de.htwg.se.bettler.controller.*
import de.htwg.se.bettler.model.gameComponent.Game

import concurrent.duration.DurationInt



//****************************************************************************** CLASS DEFINITION
class ControllerRestAPI(controller:ControllerInterface):

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val RestUIPort = 8085
  val routes: String =
    """
         """.stripMargin
  var exit = false

  //val serverUri = s"http://thecore:8080/controller/"

  val serverUriPersistence = s"http://localhost:8085/persistence/"
  private val http = Http()

  def getRequest(path: String): Future[HttpResponse] = {
    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = serverUriPersistence + path
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

  val route: Route =
    concat(
      pathSingleSlash {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, routes))
      },
      get {
        path("controller" / "newgame") {
          parameter("p1") { (p1) =>
            if p1.equals("pvp")
            then
              controller.doAndNotify(controller.newGame, "pvp")

              complete(HttpEntity(controller.toString))
            else if p1.equals("pve")
            then
              controller.doAndNotify(controller.newGame, "pve")
              complete(HttpEntity(controller.toString))
            else if p1.equals("test")
            then
              controller.doAndNotify(controller.newGame, "test")
              complete(HttpEntity(controller.toString))
            else
              complete(HttpEntity("wrong parameter"))
          }
        }
      },
      get {
        path("controller" / "test") {
          complete("test")
        }
      },

      get {
        path("controller" / "play") {
          parameter("p1") { (p1) =>

              val s = p1.split("_")
              var l = Set.empty[CardInterface]
              for (i <- 1 to s.size - 1)
                Card(s(i)) match
                  case Success(c) =>
                    l = l + c
                  case Failure(f) => println(f.getMessage)
              controller.doAndNotify(controller.play, Cards(l))
              complete(HttpEntity(controller.toString))

          }
        }
      },
      get {
        path("controller" / "skip") {
          controller.doAndNotify(controller.skip)
          complete(HttpEntity(controller.toString))
          }
      },
      get {
        path("controller" / "undo") {
          controller.undo
          complete(HttpEntity(controller.toString))
        }
      },
      get {
        path("controller" / "redo") {
          controller.redo
          complete(HttpEntity(controller.toString))
        }
      },
      get {
        path("controller" / "save") {
          controller.save
          complete(HttpEntity(controller.toString))
        }
      },
      get {
        path("controller" / "load") {
          controller.load
          complete(HttpEntity(controller.toString))
        }
      },
    )


  def start(): Unit = {
    val binding = Http().newServerAt("localhost", RestUIPort).bind(route)

    binding.onComplete {
      case Success(binding) => {
        println(s"Controller api online at ::$RestUIPort/")
      }
      case Failure(exception) => {
        println(s"Controller Api Fail ${exception.getMessage}")
      }
    }
  }