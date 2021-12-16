package de.htwg.se.bettler
package model
package cardComponent

trait CardsInterface:
    def returnSet:Set[CardInterface]
    def contains(c : CardsInterface):Boolean
    def isWorse(c: CardsInterface):Boolean
    def isPlayable : Boolean
    def remove(c : CardsInterface):CardsInterface
    def size:Int
    def groupBySameValue : Vector[CardsInterface]
    def findPlayable(board : CardsInterface) : Option[CardsInterface]

abstract class ACards extends CardsInterface