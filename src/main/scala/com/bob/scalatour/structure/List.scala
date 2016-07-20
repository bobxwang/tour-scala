package com.bob.scalatour.structure

import scala.collection.mutable.ListBuffer

sealed trait SList[+A] {
  def isEmpty: Boolean

  def head: A

  def tail: SList[A]

  def length: Int = if (isEmpty) 0 else 1 + tail.length

  def drop(n: Int): SList[A] = {
    if (isEmpty) SNil
    else if (n <= 0) this
    else tail.drop(n - 1)
  }

  //  def map[B](f: A => B): SList[B] = {
  //    if (isEmpty) SNil
  //    else f(head) :: tail.map(f)
  //  }
  //
  //  def realMap[B](f: A => B) = {
  //    val b = new ListBuffer[B]
  //    var these = this
  //    while (!these.isEmpty) {
  //      b += f(these.head)
  //      these = these.tail
  //    }
  //    b.toList
  //  }

  /**
   * @param x
   * @tparam U U必须是A的超类或A本身
   * @return
   */
  //  def ::[U >: A](x: U): SList[U] = new scala.::(x, this)
}

case object SNil extends SList[Nothing] {
  override def isEmpty: Boolean = true

  override def tail: SList[Nothing] = throw new NoSuchElementException("tail of empty list")

  override def head: Nothing = throw new NoSuchElementException("head of empty list")
}

case class SCons[+A](head: A, tail: SList[A]) extends SList[A] {
  override def isEmpty: Boolean = false
}

object SList {

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

  def sum(ints: SList[Int]): Int = ints match {
    case SNil => 0
    case SCons(x, xs) => x + sum(xs)
  }

  def sum2(ns: SList[Int]) = foldRight(ns, 0)(_ + _)

  def product(ds: SList[Double]): Double = ds match {
    case SNil => 0
    case SCons(0.0, _) => 0.0
    case SCons(x, xs) => x * product(xs)
  }

  def product2(ds: SList[Double]): Double = foldRight(ds, 0.0)((x, y) => x * y)

  val fr = foldRight(SList(1, 2, 3, 4, 5), 0) {
    _ + _
  }

  def tail[A](l: SList[A]): SList[A] =
    l match {
      case SNil => sys.error("tail of empty list")
      case SCons(_, t) => t
    }

  def setHead[A](l: SList[A], h: A): SList[A] =
    l match {
      case SNil => sys.error("setHead on empty list")
      case SCons(_, t) => SCons(h, t)
    }


  def flatmap[A, B](l: List[A])(f: A => List[B]): List[B] = ???
}