package de.htwg.se.bettler
package fileIOComponent


import model.gameComponent.Game
import java.io._
import java.io.PrintWriter
import play.api.libs.json._



trait FileIOInterface:
  def load: Game
  def save(game: Game): Unit
  def gametoJson(game: Game): JsValue
  def jsontoGame(data:String): Game

object FileIOInterface:
    def apply(): FileIOInterface =
        new fileIOJson.FileIO()