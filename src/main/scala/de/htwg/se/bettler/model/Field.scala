package de.htwg.se.bettler
package model

import gameComponent.*
import cardComponent.cardBaseImpl.Cards
import cardComponent.*

import scala.language.postfixOps

case class Field(game : Game):

  def eol = sys.props("line.separator")  

  def printCard(card : CardInterface) : String =
    var s = card.toString()
    s = s.filter(!"()".contains(_))
    val r = "[" + s + "]"
    return r


  def printField() : String =
    var r = ""
    var i = 0
    game.getPlayers()
      .map(players =>
        i += 1
          r += eol +bar() + "Player " + i + eol
          players
        .returnSet.foreach {
        r += printCard(_)
      })
    r += eol + bar()
    game.getBoard().returnSet.foreach{r+= printCard(_)}
    r += eol + bar()
    return r


  def bar(cellwidth: Int = 50) =
    "-" * cellwidth + eol