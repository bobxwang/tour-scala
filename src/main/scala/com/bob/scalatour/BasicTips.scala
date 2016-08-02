package com.bob.scalatour

import com.bob.scalatour.extens.TypeExtens._

import scala.{sys => ssys}

trait Node

case class TreeNode(v: String, left: Node, right: Node) extends Node

case class Tree(root: TreeNode)

case class User(uid: Int, name: String, age: Int, sex: Int)

object BasicTips {

  /**
   * 可变长度参数
   * @param args
   */
  def echo(args: String*) = {
    for (arg <- args) {
      println(arg)
    }
  }

  def main(args: Array[String]) {
    echo()
    echo("abc", "def")
    val arr = Array("what's", "up", "doc?")
    /* 在数组参数后加一个冒号和一个_*符号，是告诉编译器把arr的每个元素当作参数，而不是当作单一的参数传echo */
    echo(arr: _*)

    val data = Seq.fill(10)(util.Random.nextString(6))
    echo(data: _*)

    val tree = Tree(TreeNode("root", TreeNode("left", null, null), TreeNode("right", null, null)))
    tree.root match {
      case TreeNode(_, TreeNode("left", _, _), TreeNode("right", null, null)) => println("bingo")
    }

    //    findUser(19)(listUser)
    //    findUser2(6)(listUser2)

    //    testYieldInFo

  }

  def listUser(): List[User] = {
    (0 to 10).map(x => {
      User(x, s"name${x}", x, x % 2)
    }).toList
  }

  def listUser2(name: String): List[User] = {
    (0 to 10).map(x => {
      User(x, s"name${x}", x, x % 2)
    }).toList.filter(x => x.name == name)
  }

  def findUser2(uid: Int)(f: String => List[User]) = {
    lazy val u = f("name1")
    u.filter(x => x.uid == uid).foreach(println)
    println("done")
  }

  /**
   * 算n的阶乘
   * @param n
   * @return
   */
  def f(n: Int): Int = {
    @annotation.tailrec
    def go(n: Int, acc: Int): Int = {
      if (n <= 0) acc else go(n - 1, n * acc)
    }
    go(n, 1)
  }

  /**
   * 传入函数来满足多态
   * @param name
   * @param n
   * @param f 需要一个入参为Int，出参也是Int的函数
   * @return
   */
  def formatResult(name: String, n: Int, f: Int => Int) = {
    val msg = "The %s of %d is %d."
    msg.format(n, f(n))
  }

  /**
   * 柯里化同时将函数做为一个入参
   * @param uid
   * @param f
   * @return
   */
  def findUser(uid: Int)(f: () => List[User]) = {
    lazy val users = f()
    println("will invoke begin")
    users.find(x => x.uid == uid).map(println)
    println("end invoke")
    users.find(x => x.uid == 2).map(println)
  }

  def testYieldInFor() = {
    val afun = () => {
      (1 to 100).map(x => {
        x
      })
    }

    val aafun = () => {
      (101 to 200).map(x => {
        x
      })
    }
    time {
      val ab = for {
        file <- afun()
        afile <- aafun()
      } yield (file, afile)
      ab.foreach(x => println(x))

      /** 上面代码可以理解为
        * val ac = afun.flatMap(x=>aafun.map(y=>(x,y)).foreach(x=>println(x))
        */
    }

    time {
      val ab = afun().flatMap(x => {
        aafun().flatMap(y => {
          List((x, y))
        })
      })
      ab.foreach(x => println(x))
    }
  }

  def if2[A](cond: Boolean, onTrue: => A, onFalse: => A): A = if (cond) onTrue else onFalse

  val if2D = if2(false, ssys.error("fail"), 3)
}