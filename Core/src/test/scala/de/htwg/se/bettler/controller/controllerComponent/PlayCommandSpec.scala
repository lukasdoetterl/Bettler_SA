package de.htwg.se.bettler
package controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import model._
import de.htwg.se.bettler.model.stateComponent.GameStateContext
import de.htwg.se.bettler.model.gameComponent.pvpGameImpl.PvPGame
import de.htwg.se.bettler.controller.controllerBaseImp.PlayCommand
import de.htwg.se.bettler.controller.controllerBaseImp.Controller

class PlayCommandSpec extends AnyWordSpec {
    "A PlayCommand" should {
        val state = GameStateContext.getState()
        val game = PvPGame()
        val playCommand = PlayCommand(Controller(Some(game)))
        "save a memento of a game from an controller and a state" in {
            playCommand.memento.savegame shouldBe(game)
            playCommand.memento.savestate shouldBe(playCommand.memento.savestate)
        }
        "have a method doStep which sets the memento to the current game and state" in {
            playCommand.doStep
            playCommand.memento.savegame shouldBe(game)
            playCommand.memento.savestate shouldBe(GameStateContext.getState())
        }
        "have a method undoStep which saves the current game and state in the memento and overrides the game and state with the previous values from the memento" in {
            playCommand.undoStep
            playCommand.memento.savegame shouldBe(game)
            playCommand.memento.savestate shouldBe(GameStateContext.getState())
        }
        "have a method redoStep which saves the current game and state in the memento and overrides the game and state with the previous values from the memento" in {
            playCommand.redoStep
            playCommand.memento.savegame shouldBe(game)
            playCommand.memento.savestate shouldBe(GameStateContext.getState())
        }
    }
}