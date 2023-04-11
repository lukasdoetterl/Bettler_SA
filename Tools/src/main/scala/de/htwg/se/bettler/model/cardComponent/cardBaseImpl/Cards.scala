package de.htwg.se.bettler
package model
package cardComponent
package cardBaseImpl

import model.cardComponent.cardBaseImpl.Card

import scala.language.postfixOps

case class Cards(cards : Set[CardInterface]) extends CardsInterface:
    override def toString =
        var string = ""
        cards.map(c => string += c.toString)
        string

    def returnSet = cards

    def add(card : CardInterface) = Cards(cards + card)
    def add(cards : CardsInterface) = Cards(this.cards ++ cards.returnSet)
    def remove(card : CardInterface) = Cards(cards - card)
    def remove(cards : CardsInterface) = Cards(this.cards -- cards.returnSet)

    def contains(c : CardsInterface) = !c.returnSet.isEmpty && (c.returnSet -- cards).size == 0

    def isWorse(c: CardsInterface) : Boolean =
        if c.returnSet.isEmpty then return false
        if cards.size == 0 then return true
        if c.returnSet.size != cards.size then return false
        cards
          .map(c1 =>
              c.returnSet
                .withFilter(c2 => !c2.isHigher(c1))
                .foreach(c2 => return false)
          )
        return true

    def isPlayable : Boolean =
        if cards.isEmpty then return false
        cards
          .map(c1 =>
              cards
                .withFilter(c2 => !c1.sameValue(c2))
                .foreach(c2 => return false)
          )
        return true

    def size = cards.size

    def groupBySameValue: Vector[CardsInterface] = {
        val groups = (7 to 14).map { value =>
            val groupCards = cards.filter(_.intValue == value)
            if (groupCards.nonEmpty) Some(Cards(groupCards)) else None
        }.collect { case Some(cards) => cards }
        groups.toVector
    }


    def findPlayable(board: CardsInterface): Option[CardsInterface] = {
        val groupByValue = this.groupBySameValue

        groupByValue.find(board.isWorse).orElse {
            groupByValue.find(_.size > board.size).flatMap { c =>
                val reducedCards = c.returnSet.take(board.size)
                if (board.isWorse(Cards(reducedCards))) Some(Cards(reducedCards)) else None
            }
        }
    }

    def bestCards: CardsInterface = {
        val tmp = cards.head
        val highestCard = cards.foldLeft(tmp) { (acc, card) =>
            if (card.isHigher(acc)) card else acc
        }
        Cards(Set(highestCard))
    }

    def worstCards: CardsInterface = {
        val tmp = cards.head
        val lowestCard = cards.foldLeft(tmp) { (acc, card) =>
            if (acc.isHigher(card)) card else acc
        }
        Cards(Set(lowestCard))
    }