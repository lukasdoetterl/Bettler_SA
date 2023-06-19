package de.htwg.se.bettler
package database

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import de.htwg.se.bettler.fileIOComponent.database.MongoDB
import de.htwg.se.bettler.model.cardComponent.cardBaseImpl.{Card, Cards}
import de.htwg.se.bettler.model.gameComponent.pvpGameImpl.PvPGame
import de.htwg.se.bettler.model.stateComponent.GameStateContext
import de.htwg.se.bettler.model.stateComponent.stateBaseImpl.PlayerTurnState
import org.scalatest.BeforeAndAfterEach

class MongoDBSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach{
  override def beforeEach(): Unit = {
    mongoDB.deleteGame(1)
  }
  private val mongoDB = new MongoDB()

  "A MongoDB" should {

    "store and load a game" in {

      val game = PvPGame(
        Vector(
          Cards(Set(Card("CA").get)),
          Cards(Set(Card("DA").get))
        ),
        Cards(Set.empty),
        "loaded successfully"
      )
      GameStateContext.setState(PlayerTurnState(0, 2))
      mongoDB.save(game)
      val loadedGame = mongoDB.load(Some(1))
      loadedGame shouldEqual game
    }

    "delete a game" in {
      val game = PvPGame(
        Vector(
          Cards(Set(Card("CA").get)),
          Cards(Set(Card("DA").get)),
          Cards(Set(Card("HA").get))
        ),
        Cards(Set.empty),
        "loaded successfully"
      )
      GameStateContext.setState(PlayerTurnState(0, 2))
      mongoDB.save(game)
      mongoDB.deleteGame(1)
      an[NoSuchElementException] should be thrownBy mongoDB.load(Some(1))
    }


    "save a Game with the id 2" in {
      val game = PvPGame(
        Vector(
          Cards(Set(Card("CA").get)),
          Cards(Set(Card("DA").get)),
        ),
        Cards(Set.empty),
        "loaded successfully"
      )
      GameStateContext.setState(PlayerTurnState(0, 2))
      mongoDB.save(game)
      mongoDB.load(Some(2)) shouldEqual game

    }

  }
}