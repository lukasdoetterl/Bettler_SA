package de.htwg.se.bettler
package fileIOComponent
package database

//project imports

//libaries

import de.htwg.se.bettler.model.cardComponent.{CardInterface, CardsInterface}
import de.htwg.se.bettler.model.cardComponent.cardBaseImpl.Cards

import java.sql.SQLNonTransientException
import slick.lifted.TableQuery
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api.*

import scala.util.{Failure, Success, Try}
import concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import de.htwg.se.bettler.model.gameComponent.Game
import de.htwg.se.bettler.model.gameComponent.pvpGameImpl.PvPGame
import de.htwg.se.bettler.model.stateComponent.GameStateContext
import de.htwg.se.bettler.model.stateComponent.stateBaseImpl.PlayerTurnState
import play.api.libs.json.{JsObject, Json}
import fileIOComponent.database.DAOInterface
import fileIOComponent.database.sqlTables.GameTable

import de.htwg.se.bettler.model.cardComponent.cardBaseImpl.Card



val WAIT_TIME = 5.seconds
val WAIT_DB = 5000

class SlickDAO extends DAOInterface {

  val databaseDB: String =  "bettler_DB"
  val databaseUser: String =  "root"
  val databasePassword: String =  "admin"
  val databasePort: String =  "3306"
  val databaseHost: String =  "localhost"
  val databaseUrl = s"jdbc:mysql://$databaseHost:$databasePort/$databaseDB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&autoReconnect=true"
  println(databaseUrl)
  val database = Database.forURL(
    url = databaseUrl,
    driver = "com.mysql.cj.jdbc.Driver",
    user = databaseUser,
    password = databasePassword
  )


  val gameTable = new TableQuery(new GameTable(_))


  val setup: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(gameTable.schema.createIfNotExists)
  println("create tables")
  try {
    Await.result(database.run(setup), WAIT_TIME)
  } catch  {
    case e: SQLNonTransientException =>
      println("Waiting for DB connection")
      Thread.sleep(WAIT_DB)
      Await.result(database.run(setup), WAIT_TIME)
  }
  println("tables created")

  override def save(game: Game): Unit =
    Try {
      println("saving game in MySQL")
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

      val gameId =
        storeGame(
          maxplayer,
          turn,
          player1CardCount,
          player1CardsString,
          player2CardCount,
          player2CardsString,
          boardCardCount,
          boardCardsString
        )
      println(s"Game saved in MySQL with ID $gameId")
    }

  override def load(id: Option[Int] = None): Game =
    
      val query = id.map(id => gameTable.filter(_.id === id))
        .getOrElse(gameTable.filter(_.id === gameTable.map(_.id).max))

      val game = Await.result(database.run(query.result), WAIT_TIME)

      println("loading game from MySQL")

      val maxPlayer = game.head._2
      val turn = game.head._3
      val player1CardCount = game.head._4
      val player1CardsString = game.head._5
      val player2CardCount = game.head._6
      val player2CardsString = game.head._7
      val boardCardCount = game.head._8
      val boardCardsString = game.head._9

      var p1cards: CardsInterface = Cards(Set.empty[CardInterface])
      var p2cards: CardsInterface = Cards(Set.empty[CardInterface])
      var bCards: CardsInterface = Cards(Set.empty[CardInterface])

      val player1Cards = player1CardsString.split(",").toList    
      val player2Cards = player2CardsString.split(",").toList
      val boardCards = boardCardsString.split(",").toList

      //if statements are for checking if the cards are empty
      if player1CardCount != 0 then
        player1Cards
        .map(i =>
              Card((i).toString) match
                case Success(c) => p1cards = p1cards.add(c)
                case Failure(e) => e.printStackTrace)
      if player2CardCount != 0 then  
        player2Cards
        .map(i =>
              Card((i).toString) match
                case Success(c) => p2cards = p2cards.add(c)
                case Failure(e) => e.printStackTrace)
      if boardCardCount != 0 then  
        boardCards
        .map(i =>
              Card((i).toString) match
                case Success(c) => bCards = bCards.add(c)
                case Failure(e) => e.printStackTrace)
          
  //

      //make a for loop and as max use player1CardCount
  
      GameStateContext.setState(PlayerTurnState(turn, maxPlayer))
      PvPGame(Vector(p1cards,p2cards), bCards, "loaded sucessfully")

    

  
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
    val game = (
      0,
      maxPlayer,
      Turn,
      player1CardCount,
      player1Cards,
      player2CardCount,
      player2CardsString,
      BoardCardCount,
      BoardCards
    )
    val query = gameTable returning gameTable.map(_.id)
    val action = query += game
    val result = database.run(action)
    Await.result(result, WAIT_TIME)
  }


  override def deleteGame(id: Int): Try[Boolean] =
    Try{
      Await.result(database.run(gameTable.filter(_.id === id).delete), WAIT_TIME)
      true
    }



  def sanitize(str: String): String =
    str.replace("\\n", "\n")
      .replace("\\r", "\r")
      .replace("\\t", "\t")
      .replace("\\b", "\b")
      .replace("\\f", "\f")
      .replace("\\\\", "\\")
      .replace("\\\"", "\"")
      .replace("\\'", "'")
      .replace("\"\"", "\"")
}