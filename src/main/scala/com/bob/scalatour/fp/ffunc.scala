package com.bob.scalatour.fp

case class Box[A](a: A)

trait SBox[A] {
  def get: A

  // 如果SBox是Functor，就必须实现map函数, Functor其实就是可以对包装过的值做处理的函数
  // yeah,this is exactly what a functor is,a value with context,that contains an inner value with an associated context
  def map[B](f: A => B): SBox[B] = SBox(f(get))

  // 有了apply后，那么，SBOX就是一个Applicative，Applicative extends Functor，
  // 同样是对F[_]内元素进行函数施用，不同的是这个函数也是包在高阶类型，是F[A=>B]，
  // 可以对所有可游览结构(Traversable)，包括折叠(Foldable)，嵌入的元素进行函数调用
  def apply[B](f: SBox[A => B]): SBox[B] = SBox(f.get(get))

  // 有了flatMap后，那么，SBox就是一个Monad，Monad extends Applicative，
  // 更重要的是，Monad成就了for-comprehension，通过此可以实现"行令编程模式(imperative programming)"，
  // 跟传统的行令编程模式最大分别就是泛函编程中没有变量声明，变量是包嵌在一个结构里的(MyData[data])
  def flatMap[B](f: A => SBox[B]): SBox[B] = f(get)
}

object SBox {
  def apply[A](a: A) = new SBox[A] {
    override def get: A = a
  }

  /**
   * Monad is a structure that represents sequential computations.
   * The type of monad defines the means to chain the various operations together or we can say the nesting of functions of same type. This allows the programmer to build the pipleline, which is used to process the data in a sequence of steps.
   * In the monad the output of a calculation at any step is the input to the other calculation which runs as a parent to the current step. So each action is decorated with additional processing rules provided by the monad.
   */
}

object ffunc {

  // Functor
  def map[A, B](f: A => B): Box[A] => Box[B] = {
    ba: Box[A] => Box(f(ba.a))
  }

  // Monad
  def flatMap[A, B](f: A => Box[B]): Box[A] => Box[B] = {
    ba: Box[A] => f(ba.a)
  }

  // Applicative
  def apply[A, B](f: Box[A => B]): Box[A] => Box[B] = {
    ba: Box[A] => Box(f.a(ba.a))
  }

  def main(args: Array[String]) {

    val funcMapTransform = map { (s: String) => s.length }
    // 成功定义了个Functor，即对包装过的值做处理的函数
    val a = funcMapTransform(Box("hello world"))
    println(a)

    val funcFlatMapTransform = flatMap { (s: String) => Box(s.length) }
    // flatMap就是一个Monad函数
    val b = funcFlatMapTransform(Box("hello world"))
    println(b)

    // apply就是一个Applicative函数
    val funcApplyTransform = apply(Box((s: String) => s.length))
    val c = funcApplyTransform(Box("hello world"))
    println(c)

    /**
     * 这些数据类型自提供了操作函数对嵌在内部的变量进行更新，也就是说他们应该自带操作函数
     */
  }
}

object ocommonImp {

  trait Box[A] {
    def get: A
  }

  object Box {
    def apply[A](a: A) = new Box[A] {
      override def get: A = a
    }
  }

  class BoxOps[A](ba: Box[A]) {
    def map[B](f: A => B): Box[B] = Box(f(ba.get))

    def flatMap[B](f: A => Box[B]): Box[B] = f(ba.get)

    def apply[B](f: Box[A => B]): Box[B] = Box(f.get(ba.get))
  }

  implicit def toBoxOps[A](ba: Box[A]) = new BoxOps(ba)

  def run(): Unit = {
    val bxHello = Box("hello")
    println(bxHello.map(x => x.length).get)
    println(bxHello.flatMap(x => Box(x.length)).get)

    def lengthOf(s: String) = s.length

    println(bxHello.apply(Box(lengthOf _)).get)

    /**
     * 有了Monad特性后，我们就可以在for-comprehension这个封闭的环境里进行行令编程
     */
    val word = for {
      x <- Box("hello")
      y = x.length
      z <- Box(" world")
      w = x + z
    } yield w
    println(word.get)
  }
}

object omonadImp {

  trait Functor[F[_]] {
    def map[A, B](a: F[A])(f: A => B): F[B]

    def unzip[A, B](fab: F[(A, B)]): (F[A], F[B]) = {
      (map(fab) { a => a._1 }, map(fab) { a => a._2 })
    }
  }

  object ListFunctor extends Functor[List] {
    override def map[A, B](a: List[A])(f: (A) => B): List[B] = a.map(f)
  }

  object OptionFunctor extends Functor[Option] {
    override def map[A, B](a: Option[A])(f: (A) => B): Option[B] = a map f
  }

  trait Applicative[F[_]] extends Functor[F] {
    def unit[A](a: A): F[A]

    def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = {
      apply(fb)(map(fa)(f.curried))
    }

    /*
    map2跟unit是Applicative的最基本函数组件，因为apply会委托给他们两个
     */

    def apply[A, B](fa: F[A])(fab: F[A => B]): F[B] = {
      map2(fab, fa)((f, a) => f(a))
    }

    def map[A, B](fa: F[A])(f: A => B): F[B] = {
      map2(unit(f), fa)((f, a) => f(a))
    }

    def mapByApply[A, B](fa: F[A])(f: A => B): F[B] = {
      apply(fa)(unit(f))
    }
  }

  trait Monad[M[_]] extends Applicative[M] {

    def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B] = {
      join(map(ma)(f))
    }

    def compose[A, B, C](f: A => M[B], g: B => M[C]): A => M[C] = {
      a => flatMap(f(a))(g)
    }

    def join[A](mma: M[M[A]]): M[A] = {
      flatMap(mma) { ma => ma }
    }

    override def apply[A, B](fa: M[A])(fab: M[(A) => B]): M[B] = {
      flatMap(fab)(f => flatMap(fa)(a => unit(f(a))))
    }
  }

  val listMonad = new Monad[List] {
    override def unit[A](a: A): List[A] = List(a)
  }

  listMonad.map(List(1, 2, 3)) {
    _ + 10
  } // the result is List(11,12,13)
  listMonad.map2(List(1, 2), List(3, 4)) { (a, b) => List(a, b) }
  // the result is List(List(1,3),List(1,4),List(2,3),List(2,4))

  val optionMonad = new Monad[Option] {
    override def unit[A](a: A): Option[A] = Some(a)

    override def map2[A, B, C](ma: Option[A], mb: Option[B])(f: (A, B) => C): Option[C] = {
      (ma, mb) match {
        case (Some(a), Some(b)) => Some(f(a, b))
        case _ => None
      }
    }
  }

  optionMonad.map(Some(1))(_ + 1) // 2
  optionMonad.map2(Some(1), Some(2))(_ + _) // 3
}