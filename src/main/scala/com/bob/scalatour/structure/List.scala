package com.bob.scalatour.structure

sealed trait SList[+A]

case object SNil extends SList[Nothing]

case class SCons[+A](head: A, tail: SList[A]) extends SList[A]

object SList {

  def sum(ints: SList[Int]): Int = ints match {
    case SNil => 0
    case SCons(x, xs) => x + sum(xs)
  }

  def product(ds: SList[Double]): Double = ds match {
    case SNil => 0
    case SCons(0.0, _) => 0.0
    case SCons(x, xs) => x * product(xs)
  }

  def apply[A](as: A*): SList[A] = {
    if (as.isEmpty) SNil else SCons(as.head, apply(as.tail: _*))
  }

  val example = SCons(1, SCons(2, SCons(3, SNil)))
  val example2 = SList(1, 2, 3)
  println(sum(example))
  println(sum(example2))

  val x = SList(1, 2, 3, 4, 5) match {
    case SCons(x, SCons(2, SCons(4, _))) => x
    case SNil => 42
    case SCons(x, SCons(y, SCons(3, SCons(4, _)))) => x + y
    case SCons(h, t) => h + sum(t)
    case _ => 101
  }

  def append[A](a1: SList[A], a2: SList[A]): SList[A] =
    a1 match {
      case SNil => a2
      case SCons(h, t) => SCons(h, append(t, a2))
    }

  def foldRight[A, B](l: SList[A], z: B)(f: (A, B) => B): B =
    l match {
      case SNil => z
      case SCons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  val fr = foldRight(SList(1, 2, 3, 4, 5), 0) {
    _ + _
  }

  def map[A, B](l: List[A])(f: A => B): List[B] = ???

  def flatmap[A, B](l: List[A])(f: A => List[B]): List[B] = ???
}