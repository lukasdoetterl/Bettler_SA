package de.htwg.se.bettler
package model

import gameComponent.*
import cardComponent.cardBaseImpl.Cards
import cardComponent.*

import scala.language.postfixOps

case class Field(game : Game):

  def eol = sys.props("line.separator")

  def printCard(card: CardInterface): String =
    val s = card.toString().filter(!"()".contains(_))
    return "[" + s + "]"


  def printField(): String = {
    val playerStrings = game.getPlayers().zipWithIndex.map { case (players, i) =>
      eol + bar() + "Player " + (i+1) + eol + players.returnSet.map(printCard).mkString("")
    }
    val boardString = eol + bar()  + game.getBoard().returnSet.map(printCard).mkString("") + eol + bar()
    playerStrings.mkString(eol) + eol + boardString
  }


  def bar(cellwidth: Int = 50) =
    "-" * cellwidth + eol