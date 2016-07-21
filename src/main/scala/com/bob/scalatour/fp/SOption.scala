package com.bob.scalatour.fp

/**
 *
 */
sealed trait SOption[+A] {

  def map[B](f: A => B): SOption[B] = this match {
    case SNone => SNone
    case SSome(a) => SSome(f(a))
  }

  def getOrElse[B >: A](default: => B): B = this match {
    case SNone => default
    case SSome(a) => a
  }

  def orElse[B >: A](ob: => SOption[B]): SOption[B] = this.map(SSome(_)).getOrElse(ob)

  def orElse1[B >: A](ob: => SOption[B]): SOption[B] = this match {
    case SNone => ob
    case SSome(a) => this
  }

  def filter(f: A => Boolean): SOption[A] = this match {
    case SSome(a) if f(a) => this
    case _ => SNone
  }

  def filter1(f: A => Boolean): SOption[A] = flatMap(a => if (f(a)) SSome(a) else SNone)

  def flatMap[B](f: A => SOption[B]): SOption[B] = map(f) getOrElse SNone

  def flatMap1[B](f: A => SOption[B]): SOption[B] = this match {
    case SNone => SNone
    case SSome(a) => f(a)
  }
}

case class SSome[+A](get: A) extends SOption[A]

case object SNone extends SOption[Nothing]

object SOption {

  def map2[A, B, C](a: SOption[A], b: SOption[B])(f: (A, B) => C): SOption[C] = a.flatMap(aa => b.map(bb => f(aa, bb)))
}