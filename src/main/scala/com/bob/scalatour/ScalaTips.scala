package com.bob.scalatour

import com.bob.scalatour.extens.TypeExtens._

class ScalaTips {

  /**
   * "Side effects"是这样一个动作，除了返回一个值外，外部函数或者表达式还观察到此动作还有以下行为之一:
   * 有输入输出操作 (比如文件，网络I/O)
   * 对外部变量有修改
   * 外部对象的状态有改变
   * 抛出异常
   */

  /**
   * x参数是non-strict的，在函数体内每次使用都会重新计算一次，可以使用lazy来缓存，lazy不但能延后还有缓存的作用，在真正使用变量前，一旦赋值后不再重新计算。
   * @param x
   */
  def callbyName(x: => Int) = {
    println("x1=" + x)
    println("x2=" + x)
  }

  def something() = {
    println("calling something")
    1
  }

  def callByValue(x: Int) = {
    println("x1=" + x)
    println("x2=" + x)
  }

  /**
   * callByValue(something()) --> it will display calling something x1=1 x2=2
   * callByName(something()) --> it will display calling something x1=1 calling something x2=2
   * This is because call-by-value functions compute the passed-in expression's value before calling the function, thus the same value is accessed every time.
   * However, call-by-name functions recompute the passed-in expression's value every time it is accessed.
   */

  /**
   * 模拟shell管道
   */
  def usingpipe() = {
    Map("a" -> "bc", "d" -> "ef") | println
  }

  /* 在Scala里，所有的类型推导都是局部的。Scala一次只考虑一个表达式。*/

  /**
   * Functor(函子):表示范畴A与范畴B之间的映射，范畴对应scala中的高阶类型T[]，scala中常见的高阶类型有Option，集合，Function等
   *
   * Applicative:把类似Option这一类值当作封装过的值，Functor可以把这类值进行拆包，并执行函数映射，但如果此时函数也是被封装过的呢？
   * 而Applicative的作用就是应用封装过的函数到封装过的值
   *
   */

  /**
   * 方法和函数是不一样的，方法可以转成函数，函数则转不成方法
   * def amethod(x:Int):Int = x + 10
   * val afunction = (x:Int) => x + 10
   * 将方法转成函数，只需要在参数位置用 _来个替换，val tofunc = amethod _
   */

  /**
   * 缺点 --> 各种高级语法，隐转，不知所云，大量生成临时对象
   */
}