package de.htwg.se.bettler.controller

import com.google.inject.AbstractModule
import com.google.inject.TypeLiteral
import com.google.inject.name.Names
import com.google.inject.Binder
import net.codingwell.scalaguice.ScalaModule
import java.util.ResourceBundle.Control
import controllerBaseImp._


class controllerModule extends AbstractModule:
  override def configure() = {
    bind(classOf[ControllerInterface]).toInstance(Controller(None))
    //bind(classOf[FileIOInterface]).toInstance(fileIOJson.FileIO())
  }
