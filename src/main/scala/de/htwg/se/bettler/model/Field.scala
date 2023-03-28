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


  def printField(): String =
    var r = ""
    var i = 0
    game.getPlayers()
      .map(players =>
        i += 1
        r += eol + bar() + "Player " + i + eol
        players
          .returnSet.map {
          r += printCard(_)
        })
    r += eol + bar()
    game.getBoard().returnSet.map {
      r += printCard(_)
    }
    r += eol + bar()
    return r


  def bar(cellwidth: Int = 50) =
    "-" * cellwidth + eol