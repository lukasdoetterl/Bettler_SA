package de.htwg.se.bettler
package model
package cardComponent
package cardBaseImpl

import scala.util.Random
import cardComponent.Symbol
import scala.util.{Try, Success, Failure}

case object Deck {
    private val symList = List(Symbol.Hearts, Symbol.Diamonds, Symbol.Clubs, Symbol.Spades)
    private val vaList = List(Value.Seven, Value.Eight, Value.Nine, Value.Ten, Value.Jack, Value.Queen, Value.King, Value.Ace)
    private val deck = {
        val cards = new Array[CardInterface](32)
        var i = 0
        for (sym <- symList; va <- vaList) {
            val tryCard: Try[CardInterface] = Try(Card(sym, va))
            tryCard match {
                case Success(card) => cards(i) = card; i += 1
                case Failure(ex) => println(s"Error creating card ")
            }
        }
        cards
    }
}

case class Deck(size: Int) extends DeckInterface {
    var deck = Deck.deck

    def draw(): Cards = {
        val ran = scala.util.Random
        val l = ran.shuffle(deck.toList).take(7)
        val drawnCards = Cards(l.toSet)
        deck = deck.filterNot(l.contains)
        drawnCards
    }
}