package de.htwg.se.bettler

import com.google.inject.Guice
import de.htwg.se.bettler.controller.*
import de.htwg.se.bettler.controller.controllerBaseImp.*
import model.gameComponent.Game
import aview.tui.*
import aview.gui.*
import fileIOComponent.RestAPIPersistence
import controller.*
import controller.controllerBaseImp.Controller
import controller.ControllerAPI

import scala.language.postfixOps


@main def Main: Unit =
  
  val injector = Guice.createInjector(new BettlerModule)
  
  val persistenceApi = RestAPIPersistence()
  val controllerAPI = ControllerAPI(injector.getInstance(classOf[ControllerInterface]))
  
  controllerAPI.start()
  persistenceApi.start()
  val tui = TUI(injector.getInstance(classOf[ControllerInterface]))
  val gui = SwingGui()
  
  tui.run

