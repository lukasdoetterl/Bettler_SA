package de.htwg.se.bettler
package controller
package controllerBaseImp

import model.stateComponent.GameStateContext
import model.cardComponent.cardBaseImpl.Card
import model.cardComponent.cardBaseImpl.Cards
import model.cardComponent._

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.protobufv3.internal.compiler.PluginProtos.CodeGeneratorResponse.File
import akka.stream.ActorMaterializer
import play.api.libs.json.*

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}
import de.htwg.se.bettler.controller._
import de.htwg.se.bettler.model.gameComponent.Game

//****************************************************************************** CLASS DEFINITION
class ControllerRestAPI(controller:ControllerInterface):

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val RestUIPort = 8089
  val routes: String =
    """
         """.stripMargin

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