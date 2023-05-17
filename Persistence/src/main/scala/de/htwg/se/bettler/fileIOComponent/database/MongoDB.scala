package de.htwg.se.bettler
package fileIOComponent
package database

import de.htwg.se.bettler.model.cardComponent.cardBaseImpl.{Card, Cards}
import de.htwg.se.bettler.model.cardComponent.{CardInterface, CardsInterface}
import de.htwg.se.bettler.model.gameComponent.Game
import de.htwg.se.bettler.model.gameComponent.pvpGameImpl.PvPGame
import de.htwg.se.bettler.model.stateComponent.GameStateContext
import de.htwg.se.bettler.model.stateComponent.stateBaseImpl.PlayerTurnState
import com.mongodb.ConnectionString
import org.mongodb.scala.model.*
import org.mongodb.scala.model.Aggregates.*
import org.mongodb.scala.model.Filters.*
import org.mongodb.scala.model.Sorts.*
import org.mongodb.scala.result.{DeleteResult, InsertOneResult, UpdateResult}
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase, Observable, Observer, SingleObservable, SingleObservableFuture, documentToUntypedDocument, result}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success, Try}


class MongoDB extends DAOInterface {



  /* Init */
  private val database_pw = "mongo"
  private val database_username = "root"
  private val host = "localhost"
  private val port = "27017"

  val uri: String = s"mongodb://$database_username:$database_pw@$host:$port/?authSource=bettler"
  private val mongoClient: MongoClient = MongoClient(uri)
  println(uri)
  val database: MongoDatabase = mongoClient.getDatabase("bettler")
  println("Connected to MongoDB")
  val gameCollection: MongoCollection[Document] = database.getCollection("games")


  override def save(game: Game): Unit = {
    val turn = GameStateContext.getState().asInstanceOf[PlayerTurnState].currentPlayer
    val maxplayer = GameStateContext.getState().asInstanceOf[PlayerTurnState].maxPlayers
    val player1CardCount = game.getPlayers()(0).size
    val player2CardCount = game.getPlayers()(1).size
    val p1C = game.getPlayers()(0).returnSet
    var player1CardsString = ""
    p1C.foreach(player1CardsString += _.toString+",")
    val p2C = game.getPlayers()(1).returnSet
    var player2CardsString = ""
    p2C.foreach(player2CardsString += _.toString+",")
    val boardCardCount = game.getBoard().size
    val bC = game.getBoard().returnSet
    var boardCardsString = ""
    bC.foreach(boardCardsString += _.toString+",")
    val gameId = storeGame(
      maxplayer,
      turn,
      player1CardCount,
      player1CardsString,
      player2CardCount,
      player2CardsString,
      boardCardCount,
      boardCardsString
    )
    println(s"Game saved in MongoDB with ID $gameId")
  }

  override def load(id: Option[Int] = None): Game = {
    val query = id match {
      case Some(gameId) => gameCollection.find(equal("id", gameId))
      case None => gameCollection.find().sort(Document("id" -> -1)).limit(1)
    }
    val gameDoc = Await.result(query.headOption(), 5.seconds)
    if (gameDoc.isDefined) {
      val maxPlayer = gameDoc.get.getInteger("maxPlayer")
      val turn = gameDoc.get.getInteger("turn")
      val player1CardCount = gameDoc.get.getInteger("player1CardCount")
      val player1CardsString = gameDoc.get.getString("player1Cards")
      val player2CardCount = gameDoc.get.getInteger("player2CardCount")
      val player2CardsString = gameDoc.get.getString("player2Cards")
      val boardCardCount = gameDoc.get.getInteger("boardCardCount")
      val boardCardsString = gameDoc.get.getString("boardCards")
      var p1cards: CardsInterface = Cards(Set.empty[CardInterface])
      var p2cards: CardsInterface = Cards(Set.empty[CardInterface])
      var bCards: CardsInterface = Cards(Set.empty[CardInterface])
      val player1Cards = player1CardsString.split(",").toList
      val player2Cards = player2CardsString.split(",").toList
      val boardCards = boardCardsString.split(",").toList
      if (player1CardCount != 0) {
        player1Cards.foreach(i =>
          Card((i).toString) match {
            case Success(c) => p1cards = p1cards.add(c)
            case Failure(e) => e.printStackTrace
          })
      }
      if (player2CardCount != 0) {
        player2Cards.foreach(i =>
          Card((i).toString) match {
            case Success(c) => p2cards = p2cards.add(c)
            case Failure(e) => e.printStackTrace
          })
      }
      if (boardCardCount != 0) {
        boardCards.foreach(i =>
          Card((i).toString) match {
            case Success(c) => bCards = bCards.add(c)
            case Failure(e) => e.printStackTrace
          })
      }
      GameStateContext.setState(PlayerTurnState(turn, maxPlayer))
      PvPGame(Vector(p1cards, p2cards), bCards, "loaded successfully")
    } else {
      throw new NoSuchElementException("No game found with the given ID")
    }
  }

  override def storeGame(
                          maxPlayer: Int,
                          Turn: Int,
                          player1CardCount: Int,
                          player1Cards: String,
                          player2CardCount: Int,
                          player2CardsString: String,
                          BoardCardCount: Int,
                          BoardCards: String
                        ): Int = {
    val gameDoc = Document(
      "maxPlayer" -> maxPlayer,
      "turn" -> Turn,
      "player1CardCount" -> player1CardCount,
      "player1Cards" -> player1Cards,
      "player2CardCount" -> player2CardCount,
      "player2Cards" -> player2CardsString,
      "boardCardCount" -> BoardCardCount,
      "boardCards" -> BoardCards
    )
    val insertObservable = gameCollection.insertOne(gameDoc)
    Await.result(insertObservable.toFuture(), 5.seconds)
    gameDoc.getInteger("_id")
  }

  override def deleteGame(id: Int): Try[Boolean] = {
    val deleteObservable = gameCollection.deleteOne(equal("id", id))
    val result = Await.result(deleteObservable.toFuture(), 5.seconds)
    if (result.getDeletedCount == 1) {
      Success(true)
    } else {
      Failure(new NoSuchElementException("No game found with the given ID"))
    }
  }
}