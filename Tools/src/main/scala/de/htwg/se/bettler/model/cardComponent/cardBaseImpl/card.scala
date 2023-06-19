package de.htwg.se.bettler
package model
package cardComponent
package cardBaseImpl

import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions._

import java.awt.Image
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO
import scala.annotation.meta.setter
import scala.util.Failure
import scala.util.Success
import scala.util.Try
import cardComponent.Symbol
    
case class Card(symbol : Symbol, value : Value) extends CardInterface:
    override def toString = symbol.toString + value.toString
    def image = new File(f"Tools/src/main/scala/de/htwg/se/bettler/model/cardcomponent/cardpictures/" + symbol.toString + value.toString + ".png")
    def sameValue(card : CardInterface) = this.value == card.getValue
    def compareValue(card : CardInterface)(f: (Int, Int) => Boolean) = f(this.value.getValue, card.getValue.getValue)
    def isHigher(card: CardInterface) = compareValue(card)(_ > _)
    def isLower(card: CardInterface) = compareValue(card)(_ < _)
    def toCards = Cards(Set(this))
    def getSymbol = symbol
    def getValue = value
    def intValue = value.getValue


object Card :
    def apply(input: String): Try[Card] =
        val result =

            for
                symbol <- Try(input.charAt(0)).flatMap {
                    case 'H' => Success(Symbol.Hearts)
                    case 'D' => Success(Symbol.Diamonds)
                    case 'S' => Success(Symbol.Spades)
                    case 'C' => Success(Symbol.Clubs)
                    case _ => Failure(NoCardException("The string is not a card."))
                }

                value <- Try(input.drop(1)).flatMap(v => Try {
                    v match
                        case "7" => Value.Seven
                        case "8" => Value.Eight
                        case "9" => Value.Nine
                        case "10" => Value.Ten
                        case "J" => Value.Jack
                        case "Q" => Value.Queen
                        case "K" => Value.King
                        case "A" => Value.Ace
                        case _ => Value.Empty
                }).flatMap {
                    case Value.Empty => Failure(NoCardException("The string is not a card."))
                    case other => Success(other)
                }
            yield Card(symbol, value)
        result.recoverWith {
            case ex: Exception => Failure(NoCardException(s"Error parsing card: ${ex.getMessage}"))
        }


case class NoCardException(message: String) extends Exception(message) 