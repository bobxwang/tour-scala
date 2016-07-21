package com.bob.scalatour.sort


object Con {
  def unapply(l: List[Int]) = {
    val p = l(l.length / 2)
    Some((l.filter(_ < p), l.filter(_ == p), l.filter(_ > p)))
  }
}

object Quick {

  /**
   * 快排
   * @param xs
   * @return
   */
  def qsort(xs: List[Int]): List[Int] = xs match {
    case Nil => xs
    case Con(left, pivot, right) => qsort(left) ::: pivot ::: qsort(right)
  }

  def binarySearch[A](as: Array[A], key: A, f: (A, A) => Boolean): Int = {

    def go(low: Int, mid: Int, high: Int): Int = {
      if (low > high) -mid - 1
      else {
        val mid2 = (low + high) / 2
        val a = as(mid2)
        val greater = f(a, key)
        if (!greater && !f(key, a)) {
          mid2
        } else if (greater) {
          go(low, mid2, mid2 - 1)
        } else {
          go(mid2 + 1, mid2, high)
        }
      }
    }

    go(0, 0, as.length - 1)
  }

  def isSorted[A](as: Array[A], gt: (A, A) => Boolean): Boolean = {
    @annotation.tailrec
    def go(n: Int): Boolean = {
      if (n >= as.length - 1) true
      else {
        println(as(n))
        println(as(n + 1))
        if (gt(as(n), as(n + 1))) false else go(n + 1)
      }
    }
    go(0)
  }

  def findFirst[A](as: Array[A], p: A => Boolean): Int = {
    @annotation.tailrec
    def loop(n: Int): Int = {
      if (n >= as.length) -1
      else if (p(as(n))) n
      else loop(n + 1)
    }

    loop(0)
  }

}
