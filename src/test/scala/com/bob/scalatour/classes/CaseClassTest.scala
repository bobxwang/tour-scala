package com.bob.scalatour.classes

import org.scalatest.FunSuite

/**
 * 加上sealed，成为了封闭类，可以防止match时被漏掉某种可能，封闭类除了类定义所在的文件之外不能再添加任何新的子类
 */
sealed trait Animal {
  def speak: String
}

case class Tiger(speach: String) extends Animal {
  override def speak: String = speach
}

case class Shark(speach: String) extends Animal {
  override def speak: String = speach
}

case class Bear(speach: String) extends Animal {
  override def speak: String = speach
}

case object ZooKeeper {
  def openCages: Set[Animal] = Set(Tiger("prrrr"), Shark("woosh"), Bear("grrrr"))
}

case class Meter(value: Double) extends AnyVal {
  def toFeet: Foot = Foot(value * 0.3048)
}

case class Foot(value: Double) extends AnyVal {
  def toMeter: Meter = Meter(value / 0.3048)
}

class CaseClassTest extends FunSuite {

  /**
   * 模式守卫 => scala要求模式是线性的，模式变量仅允许在模式中出现一次，不过可以使用此模式来制定这个规则
   * def simplifyAdd(e:Expr) = e match {
   * case BinOp("+",x,y) if x == y => BinOp("*",x,Number(2))
   * case _ => e
   * } 模式守卫接在模式之后，开始于if，守卫可以是任意的引用模式变量的布尔表达式，如果存在模式守卫，则只有返回true的时候才算匹配成功
   */

  test("case classes match") {
    val animals = ZooKeeper.openCages
    animals.foreach(x => x match {
      case Tiger(s) => assert(s == "prrrr")
      case Shark(s) => assert(s == "woosh")
      case Bear(s) => assert(s == "grrrr")
    })
  }

  test("objectequality") {
    val t1 = Tiger("prrrr")
    val t2 = Tiger("prrrr")
    val t3 = Tiger("meow")
    assert((t1 eq t1) && (t2 eq t2) && (t3 eq t3))
    assert(t1.equals(t2))
    assert(!t1.equals(t3))
    assert(t1 == t2)
    assert(t1 != t3)
    assert(t1.hashCode == t2.hashCode)
    assert(t1.hashCode != t3.hashCode)
  }

  test("caseclasscopy") {
    val s1 = Shark("woosh")
    val s2 = s1.copy(speach = "arrrgh")
    assert(s1 == s1.copy())
    assert(s1 != s2)
  }

  test("obj to string") {
    val t1 = Tiger("prrrr")
    val t2 = Tiger("prrrr")
    assert(t1.toString == t2.toString)
  }

  test("obj apply unapply") {
    val t1 = Tiger("prrrr")
    assert(t1 == Tiger.apply(t1.speak))
    assert(Tiger.unapply(t1).get == "prrrr")
  }

  test("obj value classes") {
    assert(Meter(3.0).toFeet == Foot(0.9144000000000001))
    assert(Foot(3.0).toMeter == Meter(9.84251968503937))
  }
}