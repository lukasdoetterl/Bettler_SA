package de.htwg.se.bettler
package model
package fileIOComponent
package fileIOJson

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.bettler.model.gameComponent.Game
import de.htwg.se.bettler.model.stateComponent.GameStateContext
import de.htwg.se.bettler.model.stateComponent.stateBaseImpl.PlayerTurnState
import de.htwg.se.bettler.model.stateComponent.stateBaseImpl.StartState

class FileIOJSpec extends AnyWordSpec:
    val 1 = 1