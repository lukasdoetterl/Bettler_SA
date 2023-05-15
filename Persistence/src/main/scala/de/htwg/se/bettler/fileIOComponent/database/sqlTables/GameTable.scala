package de.htwg.se.bettler.fileIOComponent
package database
package sqlTables

import spray.json._
import slick.jdbc.MySQLProfile.api.*

class GameTable(tag: Tag) extends Table[(Int, Int, Int, Int, String, Int, String, Int, String)](tag, "GAME") :
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def maxplayer = column[Int]("MAX_PLAYER")
  
  def turn = column[Int]("TURN")
  
  def player1CardsCount = column[Int]("PLAYER1_CARDS_COUNT")
  
  def player1Cards = column[String]("PLAYER1_CARDS")
  
  def player2CardsCount = column[Int]("PLAYER2_CARDS_COUNT")
  
  def player2Cards = column[String]("PLAYER2_CARDS")
  
  def boardCardsCount = column[Int]("BOARD_CARDS_COUNT")

  def boardCards= column[String]("CARDS")
  

  override def * = (id, maxplayer, turn, player1CardsCount, player1Cards, player2CardsCount, player2Cards, boardCardsCount, boardCards)