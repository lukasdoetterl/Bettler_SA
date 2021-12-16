package de.htwg.se.bettler


import de.htwg.se.bettler.controller.controllerBaseImp._
import model._
import model.gameComponent._
import aview.TUI
import aview.gui._

@main def Main: Unit =
  val game = Game()
  val controller = Controller(game)
  val tui = TUI(controller)
  val gui = SwingGui(controller)
  tui.run
