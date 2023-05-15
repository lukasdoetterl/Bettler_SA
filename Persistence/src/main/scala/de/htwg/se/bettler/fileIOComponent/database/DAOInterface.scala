package de.htwg.se.bettler
package fileIOComponent
package database


import de.htwg.se.bettler.model.gameComponent.Game


import scala.util.Try

trait DAOInterface {
  
  def save(game: Game): Unit

  def load(id: Option[Int]): Game
  
  def storeGame(
                          maxPlayer: Int,
                          Turn: Int,
                          player1CardCount: Int,
                          player1Cards: String,
                          player2CardCount: Int,
                          player2CardsString: String,
                          BoardCardCount: Int,
                          BoardCards: String
                        ): Int
  
  def deleteGame(id: Int): Try[Boolean]


}


