package de.htwg.se.bettler
package fileIOComponent
package fileIOJson

import scala.io.Source
import java.io._
import java.io.PrintWriter
import model.gameComponent.Game
import model.cardComponent.CardsInterface

import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.name.Names

import play.api.libs.json._
import scala.io.Source
import model.cardComponent.CardInterface
import model.cardComponent.cardBaseImpl.Card
import com.google.common.base.Strings
import model.gameComponent.pvpGameImpl.PvPGame
import model.cardComponent.cardBaseImpl.Cards
import scala.util.Success
import scala.util.Failure
import model.stateComponent.GameStateContext
import model.stateComponent.stateBaseImpl.PlayerTurnState

class FileIO extends FileIOInterface:

    override def load : Game =
        print("trying to get STring from file")
        val source: String = Source.fromFile("game.json").getLines.mkString
        print("got json from file")

        val json: JsValue = Json.parse(source)
        GameStateContext.setState(PlayerTurnState((json \ "turn").as[Int], (json \ "maxplayer").as[Int]))
        var p1cards : CardsInterface = Cards(Set.empty[CardInterface])
        var p2cards : CardsInterface = Cards(Set.empty[CardInterface])
        var boardc : CardsInterface = Cards(Set.empty[CardInterface])
        val player1 = (json \ "player1")
        val player2 = (json \ "player2")
        val board = (json \ "board")
        val msg = (json \ "message").as[String]
        (0 to (player1 \ "anzahl").as[Int] - 1)
          .foreach(i =>
              val value = (player1.get \\ "value") (i).as[String]
              val symbol = (player1.get \\ "symbol") (i).as[String]
                Card (symbol + value) match
                  case Success(c) => p1cards = p1cards.add(c)
                  case Failure(e) => e.printStackTrace)
        (0 to (player2 \ "anzahl").as[Int] - 1)
          .map(i =>
              val value = (player2.get \\ "value") (i).as[String]
              val symbol = (player2.get \\ "symbol") (i).as[String]
                Card (symbol + value) match
                  case Success(c) => p2cards = p2cards.add(c)
                  case Failure(e) =>)
        (0 to (board \ "anzahl").as[Int] - 1)
          .map(i =>
              val value = (board.get \\ "value") (i).as[String]
              val symbol = (board.get \\ "symbol") (i).as[String]
                Card (symbol + value) match
                  case Success(c) => boardc = boardc.add(c)
                  case Failure(e) =>)
        PvPGame(Vector(p1cards, p2cards), boardc, msg)

       

    override def jsontoGame(data:String): Game =
        
        val json: JsValue = Json.parse(data)
        GameStateContext.setState(PlayerTurnState((json \ "turn").as[Int], (json \ "maxplayer").as[Int]))
        var p1cards: CardsInterface = Cards(Set.empty[CardInterface])
        var p2cards: CardsInterface = Cards(Set.empty[CardInterface])
        var boardc: CardsInterface = Cards(Set.empty[CardInterface])
        val player1 = (json \ "player1")
        val player2 = (json \ "player2")
        val board = (json \ "board")
        val msg = (json \ "message").as[String]
        (0 to (player1 \ "anzahl").as[Int] - 1)
          .map(i =>
              val value = (player1.get \\ "value") (i).as[String]
              val symbol = (player1.get \\ "symbol") (i).as[String]
                Card (symbol + value) match
                  case Success(c) => p1cards = p1cards.add(c)
                  case Failure(e) => e.printStackTrace)
        (0 to (player2 \ "anzahl").as[Int] - 1)
          .map(i =>
              val value = (player2.get \\ "value") (i).as[String]
              val symbol = (player2.get \\ "symbol") (i).as[String]
                Card (symbol + value) match
                  case Success(c) => p2cards = p2cards.add(c)
                  case Failure(e) =>)
        (0 to (board \ "anzahl").as[Int] - 1)
          .map(i =>
              val value = (board.get \\ "value") (i).as[String]
              val symbol = (board.get \\ "symbol") (i).as[String]
                Card (symbol + value) match
                  case Success(c) => boardc = boardc.add(c)
                  case Failure(e) =>)
        PvPGame(Vector(p1cards, p2cards), boardc, msg)

    override def save(game : Game) = 
        import java.io._
        val pw = new PrintWriter(new File("game.json"))
        pw.write(Json.prettyPrint(Json.toJson[Game](game)))
        pw.close


    override def gametoJson(game: Game): JsValue = Json.obj(
        "turn" -> GameStateContext.getState().asInstanceOf[PlayerTurnState].currentPlayer,
        "maxplayer" -> GameStateContext.getState().asInstanceOf[PlayerTurnState].maxPlayers,
        "player1" -> Json.obj(
            "anzahl" -> game.getPlayers()(0).size,
            "karten" -> Json.toJson(
                for {
                    p <- game.getPlayers()(0).returnSet
                } yield {
                    Json.obj(
                        "symbol" -> p.getSymbol.toString,
                        "value" -> p.getValue.toString
                    )
                }
            )),
        "player2" -> Json.obj(
            "anzahl" -> game.getPlayers()(1).size,
            "karten" -> Json.toJson(
                for {
                    p <- game.getPlayers()(1).returnSet
                } yield {
                    Json.obj(
                        "symbol" -> p.getSymbol.toString,
                        "value" -> p.getValue.toString
                    )
                }
            )),
        "board" -> Json.obj(
            "anzahl" -> game.getBoard().size,
            "karten" -> Json.toJson(
                for {
                    p <- game.getBoard().returnSet
                } yield {
                    Json.obj(
                        "symbol" -> p.getSymbol.toString,
                        "value" -> p.getValue.toString
                    )
                }
            )),
        "message" -> game.getMessage(),
    )


    implicit val gameWrites : Writes[Game] = new Writes[Game] {
        def writes(game : Game):JsValue = Json.obj(
            "turn" -> GameStateContext.getState().asInstanceOf[PlayerTurnState].currentPlayer,
            "maxplayer" -> GameStateContext.getState().asInstanceOf[PlayerTurnState].maxPlayers,
            "player1" -> Json.obj(
                "anzahl" -> game.getPlayers()(0).size,
                "karten" -> Json.toJson(
                    for {
                        p <- game.getPlayers()(0).returnSet
                    } yield {
                        Json.obj(
                            "symbol" -> p.getSymbol.toString,
                            "value" -> p.getValue.toString
                        )
                    }
            )),
            "player2" -> Json.obj(
                "anzahl" -> game.getPlayers()(1).size,
                "karten" -> Json.toJson(
                    for {
                        p <- game.getPlayers()(1).returnSet
                    } yield {
                        Json.obj(
                            "symbol" -> p.getSymbol.toString,
                            "value" -> p.getValue.toString
                        )
                    }
            )),
            "board" -> Json.obj(
                "anzahl" -> game.getBoard().size,
                "karten" -> Json.toJson(
                    for {
                        p <- game.getBoard().returnSet
                    } yield {
                        Json.obj(
                            "symbol" -> p.getSymbol.toString,
                            "value" -> p.getValue.toString
                        )
                    }
            )),
            "message" -> game.getMessage(),
        )
    }

