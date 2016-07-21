package com.bob.scalatour.fp

/**
 *
 */
object Started {

  def compose[A, B, C](f: B => C, g: A => B): A => C = a => f(g(a))

  def curry[A, B, C](f: (A, B) => C): A => (B => C) = a => b => f(a, b)

  def uncurry[A, B, C](f: A => B => C): (A, B) => C = (a, b) => f(a)(b)

  /**
   * 接受一个参数，一个函数需要两个入参，同时返回一个需要一个入参数的函数
   * @param a
   * @param f
   * @tparam A
   * @tparam B
   * @tparam C
   * @return
   */
  def partial[A, B, C](a: A, f: (A, B) => C): B => C = (b: B) => f(a, b)

  def main(args: Array[String]) {
    val a = compose[Int, Int, Int]((x: Int) => x + 1, (x: Int) => x + 1)
    println(a(2))

    val b = curry[Int, Int, Int]((x, y) => (x + 100) * y)
    println(b(2)(3))
  }
}