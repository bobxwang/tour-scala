package com.bob.scalatour.structure

sealed trait SList[+A] {
  def isEmpty: Boolean

  def head: A

  def tail: SList[A]

  def length: Int = if (isEmpty) 0 else 1 + tail.length

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

  @annotation.tailrec
  def foldLeft[A, B](l: SList[A], z: B)(f: (B, A) => B): B = l match {
    case SNil => z
    case SCons(h, t) => foldLeft(t, f(z, h))(f)
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

  def drop[A](l: SList[A], n: Int): SList[A] = {
    if (n <= 0) l
    else {
      l match {
        case SNil => SNil
        case SCons(_, t) => drop(t, n - 1)
      }
    }
  }

  def dropWhile[A](l: SList[A], f: A => Boolean): SList[A] = l match {
    case SCons(h, t) if f(h) => dropWhile(t, f)
    case _ => l
  }

  def init[A](l: SList[A]): SList[A] = l match {
    case SNil => sys.error("init of empty list")
    case SCons(_, SNil) => SNil
    case SCons(h, t) => SCons(h, init(t))
  }

  def init2[A](l: SList[A]): SList[A] = {
    import scala.collection.mutable.ListBuffer
    val buf = new ListBuffer[A]
    @annotation.tailrec
    def go(cur: SList[A]): SList[A] = cur match {
      case SNil => sys.error("init of empty list")
      case SCons(_, SNil) => SList(buf.toList: _*)
      case SCons(h, t) => buf += h; go(t)
    }
    go(l)
  }

  def concat[A](l: SList[SList[A]]): SList[A] = foldRight(l, SNil: SList[A])(append)

  def length[A](l: SList[A]): Int = foldRight(l, 0)((_, y) => y + 1)

  def map[A, B](l: SList[A])(f: A => B): SList[B] = foldRight(l, SNil: SList[B])((h, t) => SCons(f(h), t))

  def flatmap[A, B](l: SList[A])(f: A => SList[B]): SList[B] = concat(map(l)(f))

}