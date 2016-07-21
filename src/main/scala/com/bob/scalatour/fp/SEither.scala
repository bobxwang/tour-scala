package com.bob.scalatour.fp

/**
 * Created by bob on 16/7/21.
 */
sealed trait SEither[+E, +A] {

  def map[B](f: A => B): SEither[E, B] = this match {
    case SRight(a) => SRight(f(a))
    case SLeft(e) => SLeft(e)
  }

}

case class SLeft[+E](get: E) extends SEither[E, Nothing]

case class SRight[+A](get: A) extends SEither[Nothing, A]
