package de.htwg.se.bettler
package model
package cardComponent
package cardBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class DeckSpec extends AnyWordSpec:
   "Deck" should {
      var deck = Deck(32)
      var deck2 = Deck(32)
      "Have a method for drawing seven cards from a deck" in {
         deck.draw().size should be (7)
      }
   }