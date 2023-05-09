package de.htwg.se.bettler.controller

import de.htwg.se.bettler.controller.controllerBaseImp.ControllerRestAPI
import com.google.inject.Guice
object controllerMain {
  @main def run =
    val injector = Guice.createInjector(new controllerModule)
    val controller = injector.getInstance(classOf[ControllerInterface])
    
    ControllerRestAPI(controller).start()
}
