package de.htwg.se.bettler.cardgenerating

//****************************************************************************** IMPORTS

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{entity, *}
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.stream.ActorMaterializer
import de.htwg.se.bettler.model.cardComponent.Value
import de.htwg.se.bettler.model.cardComponent.Symbol



//****************************************************************************** CLASS DEFINITION
class RestAPIcardgenerating():

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val symbol = Symbol
  val value =  Value


  val RestUIPort = 8085
  val routes: String =
    """
         """.stripMargin

  val route: Route =
    concat(
      pathSingleSlash {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, routes))
      },
      get {
        path("cardgeneration" / "applySymbol") {
          parameter("p1") { (p1) =>

            complete(HttpEntity(symbol.apply(p1).toString))
          }
        }
      },
      get {
        path("cardgeneration" / "applyValue") {
          parameter("p1") { (p1) =>

            complete(HttpEntity(value.apply(p1).toString))
          }
        }
      },
      get {
        path("cardgneration" / "test") {
          complete("test")
        }
      },
      put {
        path("cardgneration" / "store") {
          entity(as[String]) { data =>
            complete {
              Future.successful(HttpEntity(ContentTypes.`text/html(UTF-8)`, "game successfully saved"))
            }
          }
        }
      }
    )

  def start(): Unit = {
    val binding = Http().newServerAt("localhost", RestUIPort).bind(route)

    binding.onComplete {
      case Success(binding) => {
        println(s"card generation apiu online at ::$RestUIPort/")
      }
      case Failure(exception) => {
        println(s"Card generation  Api Fail ${exception.getMessage}")
      }
    }
  }