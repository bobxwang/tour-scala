package com.bob.scalatour.fp

/**
 * Monoid
		--> 一个抽象顾类型A，
		--> 一个二无结合性函数(对传入的两个A类参数进行操作返回一个A) --> op(a,op(b,c)) = op(op(a,b),c)
		--> 一个恒等值
 */
trait Monoid[A] {

  def op(a1: A, a2: A): A

  def zero: A
}

object Monoid {

  val stringMonoid = new Monoid[String] {
    override def op(a1: String, a2: String): String = a1 + a2

    override def zero: String = ""
  }

  def listMonoid[A] = new Monoid[List[A]] {
    override def op(a1: List[A], a2: List[A]): List[A] = a1 ++ a2

    override def zero: List[A] = Nil
  }

  val booleanAnd: Monoid[Boolean] = new Monoid[Boolean] {
    override def op(a1: Boolean, a2: Boolean): Boolean = a1 && a2

    override def zero: Boolean = true
  }

  def endoMonoid[A]: Monoid[A => A] = new Monoid[(A) => A] {
    override def op(a1: (A) => A, a2: (A) => A): (A) => A = a1 compose a2

    override def zero: (A) => A = (a: A) => a
  }

  /**
   * List()的foldLeft方法签名
   * def foldLeft[B](z:B)(f:(B,A)=>B):B
   * 如果B,A是相同的类型，那么就是
   * def foldLeft[A](z:A)(f:(A,A)=>A):A
   * 可以看出Monoid正好满足这些方法签名，让我们以字符串来举例
   */

  val words = List("hello", "world", "how", "are", "you")
  val s = words.foldLeft(stringMonoid.zero)(stringMonoid.op)
  // it will print: helloworldhowareyou
  println(s)


  def concatenate[A](as: List[A], m: Monoid[A]): A = as.foldLeft(m.zero)(m.op)

  def foldMap[A, B](as: List[A], m: Monoid[B])(f: A => B): B = as.foldLeft(m.zero)((a, b) => m.op(a, f(b)))
}