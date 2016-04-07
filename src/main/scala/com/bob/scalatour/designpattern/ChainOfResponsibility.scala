package com.bob.scalatour.designpattern

/**
 * 基于偏函数实现
 */
object ChainOfResponsibility {

  case class Event(source: String)

  type EventHandler = PartialFunction[Event, Unit]

  val defaultHandler: EventHandler = PartialFunction(_ => ())

  val keyboardHandler: EventHandler = {
    case Event("keyboard") => println("key board event")
  }

  def mouseHandler(delay: Int): EventHandler = {
    case Event("mouse") => println("mouse event " + delay)
  }

  def main(args: Array[String]) {
    keyboardHandler.orElse(mouseHandler(10)).orElse(defaultHandler)
  }

}