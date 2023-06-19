package de.htwg.se.bettler
package model
package gameComponent

import cardComponent.*
import de.htwg.se.bettler.util.*
import pveGameImpl.*
import pvpGameImpl.*
import model.stateComponent.*
import de.htwg.se.bettler.model.cardComponent.cardBaseImpl.{Card, Cards}



trait Game extends Originator:
    def play(cards : CardsInterface) : Game
    def skip() : Game
    def getPlayers() : Vector[CardsInterface]
    def getBoard() : CardsInterface
    def getMessage() : String
    def nextRound : Game

object Game:
    def apply() : Game = Game("pvp")
    def apply(kind: String) : Game = kind match
        case "pvp" => 
            GameStateContext.handle(GameStateEvents.Start)
            PvPGame()
        case "pve" => 
            GameStateContext.handle(GameStateEvents.Start)
            PvEGame()
        case "test" =>
            GameStateContext.handle(GameStateEvents.Start)
            PvPGame(Vector(
                Cards(Set(Card(Symbol.Diamonds, Value.Ace),Card(Symbol.Diamonds, Value.King),Card(Symbol.Diamonds, Value.Queen),Card(Symbol.Diamonds, Value.Jack),Card(Symbol.Diamonds, Value.Nine),Card(Symbol.Diamonds, Value.Seven),Card(Symbol.Diamonds, Value.Ten))),
                Cards(Set(Card(Symbol.Clubs, Value.Ace),Card(Symbol.Clubs, Value.King),Card(Symbol.Clubs, Value.Queen),Card(Symbol.Clubs, Value.Jack),Card(Symbol.Clubs, Value.Nine),Card(Symbol.Clubs, Value.Seven),Card(Symbol.Clubs, Value.Ten)))
            ),Cards(Set.empty[CardInterface]),  "Player 1 turn.")
        case _ => throw new IllegalArgumentException("You Should not have messed with this Code")