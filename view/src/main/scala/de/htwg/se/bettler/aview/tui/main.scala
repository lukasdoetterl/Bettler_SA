package de.htwg.se.bettler.aview.tui

object RunTui {
  @main def run =
    println("\n" * 50)
    val tui = TUIRest()
      tui.run
}