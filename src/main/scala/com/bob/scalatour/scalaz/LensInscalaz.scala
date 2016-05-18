package com.bob.scalatour.scalaz

import scalaz.Lens

object LensInscalaz {

  case class Point(x: Double, y: Double)

  case class Color(r: Byte, g: Byte, b: Byte)

  case class Turtle(position: Point, heading: Double, color: Color)

  def main(args: Array[String]) {

    val turtlePosition = Lens.lensu[Turtle, Point]((a, value) => a.copy(position = value), _.position)
    val pointX = Lens.lensu[Point, Double]((a, value) => a.copy(x = value), _.x)
    val turtleX = turtlePosition >=> pointX

    val t0 = Turtle(Point(2.0, 3.0), 0.0, Color(255.toByte, 255.toByte, 255.toByte))
    println(turtleX.get(t0)) // 2.0
    println(turtleX.set(t0, 5.0)) // Turtle(Point(5.0,3.0),0.0,Color(-1,-1,-1))
    println(turtleX.mod(_ + 10, t0)) // Turtle(Point(12.0,3.0),0.0,Color(-1,-1,-1))

    val incX = turtleX =>= {
      _ + 5 // =>= is a currying format for symbolic variation to mod
    }
    println(incX(t0)) // Turtle(Point(7.0,3.0),0.0,Color(-1,-1,-1))

    val incy = for {
      x <- turtleX %= {
        _ + 1.0
      }
    } yield x
    println(incy(t0))
  }
}