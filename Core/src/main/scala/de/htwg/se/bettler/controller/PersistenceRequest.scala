package de.htwg.se.bettler
package controller

import de.htwg.se.bettler.controller.WebClient
import scala.concurrent.{ExecutionContextExecutor, Future}
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest, HttpResponse, StatusCode}
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.http.javadsl.model.StatusCodes
import akka.http.javadsl.model.Uri

import scala.concurrent.Await
import akka.actor.ActorSystem
import akka.stream.{Materializer, SystemMaterializer}
import akka.http.scaladsl.model.Uri

import scala.util.{Failure, Success, Try}
import play.api.libs.json.*
import akka.http.scaladsl.unmarshalling.Unmarshal

import scala.concurrent.duration.*
import fileIOComponent.*
import model.gameComponent.*
import controller.*
import de.htwg.se.bettler.fileIOComponent.fileIOJson.FileIO

import scala.concurrent.ExecutionContext.Implicits.global


class PersistenceRequest {
  val fio = new FileIO()

  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: Materializer = SystemMaterializer(system).materializer

  val webClient = new WebClient("http://localhost:8081/persistence/")

  def loadGame(resulti: Future[HttpResponse]): String = {
    var resJSON = ""
    val res = resulti.flatMap { response =>
      response.status match {
        case StatusCodes.OK =>
          Unmarshal(response.entity).to[String].map { jsonStr =>
            resJSON = jsonStr
          }
        case _ =>
          Future.failed(new RuntimeException(s"HTTP request failed with status ${response.status} and entity ${response.entity}"))
      }
    }
    Await.result(res, 10.seconds)
    resJSON
  }


  def save(game: Game): Unit = {
    val endpoint = "store"
    val putResponse = webClient.putRequest(fio.gametoJson(game).toString(), endpoint)
    val res = putResponse.flatMap { response =>
      response.status match {
        case StatusCodes.OK =>
          Unmarshal(response.entity).to[String].map { entity =>
            println(s"Request completed successfully with status ${response.status} and content:\n${entity}")
          }
        case _ =>
          Future.failed(new RuntimeException(s"HTTP request failed with status ${response.status} and entity ${response.entity}"))
      }
    }
    Await.result(res, 10.seconds)
  }

  def load():String = {
    val endpoint = "load"
    val postResponse = webClient.getRequest(endpoint)
    loadGame(postResponse)
  }
}