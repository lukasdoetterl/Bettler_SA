package de.htwg.se.bettler.aview.tui

import scala.Console.{BLUE, RESET}
import scala.io.StdIn.readLine

object main {
  @main def run =
    println("Welcome to Bettler")
    println("\n" * 50)
    val tui = TUIRest()
      tui.run
}