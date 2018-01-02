package com.bob.scalatour.designpattern

import com.twitter.logging.Level

trait Log {
  def warning(message: String)

  def error(message: String)
}

final class Logger {
  def log(level: Level, message: String): Unit = {

  }
}

/**
 * 在scala中，我们可以使用隐式类轻松搞定适配器模式
 */
object Adapter {

  implicit class LoggerAdapter(logger: Logger) extends Log {
    override def warning(message: String): Unit = logger.log(Level.WARNING, message)

    override def error(message: String): Unit = logger.log(Level.ERROR, message)
  }

  val log: Log = new Logger
}