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

}
