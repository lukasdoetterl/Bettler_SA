package de.htwg.se.bettler.aview
package gui

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
import de.htwg.se.bettler.fileIOComponent
import de.htwg.se.bettler.fileIOComponent.fileIOJson.FileIO
import de.htwg.se.bettler.model.cardComponent.{CardInterface, CardsInterface}
import de.htwg.se.bettler.model.gameComponent.Game
import de.htwg.se.bettler.model.gameComponent.pvpGameImpl.PvPGame
import de.htwg.se.bettler.util.{Observable, Observer}

import scala.concurrent.duration.*
import scala.concurrent.ExecutionContext.Implicits.global
import scala.swing.Reactor


class Request extends Observable{

  val fileio = new FileIO()
  var game: Game = Game()
  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: Materializer = SystemMaterializer(system).materializer



  val webClient = new Client("http://localhost:8080/controller/")

  def waitRefreshGame(resulti: Future[HttpResponse]) = {
    val res = resulti.flatMap { response =>
      response.status match {
        case StatusCodes.OK =>
          Unmarshal(response.entity).to[String].map { jsonStr =>
            this.game = fileio.jsontoGame(jsonStr)
          }
        case _ =>
          Future.failed(new RuntimeException(s"HTTP request failed with status ${response.status} and entity ${response.entity}"))
      }
    }
    Await.result(res, 10.seconds)
    notifyObservers
  }

  def undo(): Unit = {
    val endpoint = "undo"
    val postResponse = webClient.getRequest(endpoint)
    waitRefreshGame(postResponse)
  }

  def get(): Unit = {
    val endpoint = "get"
    val postResponse = webClient.getRequest(endpoint)
    waitRefreshGame(postResponse)
  }

  def redo(): Unit = {
    val endpoint = "redo"
    val postResponse = webClient.getRequest(endpoint)
    waitRefreshGame(postResponse)
  }

  def load(): Unit = {
    val endpoint = "load"
    val postResponse = webClient.getRequest(endpoint)
    waitRefreshGame(postResponse)
  }

  def save(): Unit = {
    val endpoint = "save"
    val postResponse = webClient.postRequest(fileio.gametoJson(this.game).toString(), endpoint)
    waitRefreshGame(postResponse)
  }

  def newGame(gcase :String): Unit = {
    val endpoint = s"newgame?gcase1=$gcase"
    val postResponse = webClient.postRequest("", endpoint)
    waitRefreshGame(postResponse)
  }

  def play(cards: String): Unit = {
    val endpoint = s"play?cards=$cards"
    val postResponse = webClient.postRequest("",endpoint)
    waitRefreshGame(postResponse)
  }

  def skip(): Unit = {
    val endpoint = "skip"
    val postResponse = webClient.postRequest(fileio.gametoJson(this.game).toString(), endpoint)
    waitRefreshGame(postResponse)
  }
  

}