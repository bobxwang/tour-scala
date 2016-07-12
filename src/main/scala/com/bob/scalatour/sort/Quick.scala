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

  def isSorted[A](as: Array[A], gt: (A, A) => Boolean): Boolean = ???

  /**
   * 接受一个参数，一个函数需要两个入参，同时返回一个需要一个入参数的函数
   * @param a
   * @param f
   * @tparam A
   * @tparam B
   * @tparam C
   * @return
   */
  def partial[A, B, C](a: A, f: (A, B) => C): B => C = ???


}
