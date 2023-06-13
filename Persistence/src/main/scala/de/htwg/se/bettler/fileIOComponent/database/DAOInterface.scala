package de.htwg.se.bettler
package fileIOComponent
package database

import de.htwg.se.bettler.model.gameComponent.Game
import scala.concurrent.Future
import scala.util.Try

trait DAOInterface {

  def save(game: Game): Future[Unit]

  def load(id: Option[Int]): Future[Game]

  def storeGame(
                 maxPlayer: Int,
                 Turn: Int,
                 player1CardCount: Int,
                 player1Cards: String,
                 player2CardCount: Int,
                 player2CardsString: String,
                 BoardCardCount: Int,
                 BoardCards: String,
                 id: Option[Int]
               ): Future[Int]

  def deleteGame(id: Int): Future[Try[Unit]]

}
