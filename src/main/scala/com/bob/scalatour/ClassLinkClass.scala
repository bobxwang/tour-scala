package com.bob.scalatour

import org.joda.time.Duration

import annotation.implicitNotFound

/**
  * 类型类
  */
@implicitNotFound("No member of type class NumberLike in scope for ${T}")
trait NumberLike[T] {
  def plus(x: T, y: T): T

  def divide(x: T, y: Int): T

  def minus(x: T, y: T): T
}

// 默认实现的伴生对象
object NumberLike {

  /**
    * 类型类的成员通常是单例对象，而且会有一个 implicit 关键字位于前面，
    * 这是类型类在 Scala 中成为可能的几个重要因素之一，在某些条件下，它让类型类成员隐式可用
    */

  implicit object NumberLikeDouble extends NumberLike[Double] {
    def plus(x: Double, y: Double): Double = x + y

    def divide(x: Double, y: Int): Double = x / y

    def minus(x: Double, y: Double): Double = x - y
  }

  implicit object NumberLikeInt extends NumberLike[Int] {
    def plus(x: Int, y: Int): Int = x + y

    def divide(x: Int, y: Int): Int = x / y

    def minus(x: Int, y: Int): Int = x - y
  }

  implicit object NumberLikeDuration extends NumberLike[Duration] {
    def plus(x: Duration, y: Duration): Duration = x.plus(y)

    def divide(x: Duration, y: Int): Duration = Duration.millis(x.getMillis / y)

    def minus(x: Duration, y: Duration): Duration = x.minus(y)
  }

}

// how to using
object ClassLinkClass extends App {

  // 方法带有一个类型参数 T ，接受类型为 Vector[T] 的参数。
  // 将参数限制在特定类型类的成员上，是通过第二个 implicit 参数列表实现的。
  // 这是什么意思？这是说，当前作用域中必须存在一个隐式可用的 NumberLike[T] 对象，
  // 比如说，当前作用域声明了一个 隐式值(implicit value)。
  // 这种声明很多时候都是通过导入一个有隐式值定义的包或者对象来实现的。
  def mean[T](xs: Vector[T])(implicit ev: NumberLike[T]): T =
  ev.divide(xs.reduce(ev.plus(_, _)), xs.size)

  val numbers = Vector[Double](13, 23.0, 42, 45, 61, 73, 96, 100, 199, 420, 900, 3839)
  println(mean(numbers))

  import org.joda.time.Duration._

  val durations = Vector(standardSeconds(20), standardSeconds(57), standardMinutes(2),
    standardMinutes(17), standardMinutes(30), standardMinutes(58), standardHours(2),
    standardHours(5), standardHours(8), standardHours(17), standardDays(1),
    standardDays(4))
  println(mean(durations).getStandardHours)
}