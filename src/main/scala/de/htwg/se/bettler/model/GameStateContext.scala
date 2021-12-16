package de.htwg.se.bettler
package model
import stateComponent._
import stateComponent.stateBaseImpl._

object GameStateContext:
    var state : State = StartState()
    def handle(e: Events) = state = state.handle(e)
    def getState() = state
    def setState(s : State) = state = s