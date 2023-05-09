package de.htwg.se.bettler.aview.tui

object main {
  @main def run =
    println("\n" * 50)
    val tui = TUIRest()
      tui.run
}