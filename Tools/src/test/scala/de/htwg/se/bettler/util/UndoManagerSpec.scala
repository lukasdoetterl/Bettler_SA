package de.htwg.se.bettler
package util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import model.stateComponent._
import model.stateComponent.stateBaseImpl.StartState
import model.stateComponent.stateBaseImpl.PlayerTurnState

import model.gameComponent._

class UndoManagerSpec extends AnyWordSpec:
    val x = 1