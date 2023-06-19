package de.htwg.se.bettler
package model
package cardComponent
package cardBaseImpl

import scala.util.Random
import cardComponent.Symbol
import scala.util.{Try, Success, Failure}

case class Deck_old(size: Int) extends DeckInterface {
    var deck = Set.empty[CardInterface]

    if (size == 32) {
        val symList = List(Symbol.Hearts, Symbol.Diamonds, Symbol.Clubs, Symbol.Spades)
        val vaList = List(Value.Seven, Value.Eight, Value.Nine, Value.Ten, Value.Jack, Value.Queen, Value.King, Value.Ace)

        for (sym <- symList; va <- vaList) {
            val tryCard: Try[CardInterface] = Try(Card(sym, va))
            tryCard match
                case Success(card) => deck += card
                case Failure(ex) => println(s"Error creating card ")
        }
    }

    def draw(): Cards = {
        val ran = scala.util.Random
        val l = ran.shuffle(deck.toList).take(7)
        val drawnCards = Cards(l.toSet)
        deck = deck -- drawnCards.cards
        drawnCards
    }
}