package de.htwg.se.bettler

import com.google.inject.Guice
import de.htwg.se.bettler.controller._
import de.htwg.se.bettler.controller.controllerBaseImp._
import model.gameComponent.Game
import aview.tui._
import aview.gui._
import de.htwg.se.bettler.fileIOComponent._
import de.htwg.se.bettler.controller._

@main def Main: Unit =
  val injector = Guice.createInjector(new BettlerModule)
  val controller = injector.getInstance(classOf[ControllerInterface])
  val tui = TUIRest()
  //val tui = TUI(controller)

  //val gui = SwingGui(controller)
  val fileIOService = RestAPIPersistence()
  val controllerService = ControllerRestAPI(controller)
  fileIOService.start()
  //test
  controllerService.start()
  tui.run


