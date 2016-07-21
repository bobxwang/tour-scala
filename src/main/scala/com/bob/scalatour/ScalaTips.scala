package com.bob.scalatour

import com.bob.scalatour.extens.TypeExtens._

object ScalaTips {

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

  /**
   * Any 超类，相当于java中的Object
   * AnyVal => 每个内建值类的父类，Byte,Short,Char,Int,Long,Float,Double,Boolean,Unit,值在这都被定义成final还是abstract,
   * 故不能new,在需要的时候，比如调用toString方法时，scala会自动对其进行装箱动作，透明的转成java.lang.Integer,
   * scala中的相等操作==被设计为对类型表达透明，不像java，对字符串等引用类型需要用equals
   * AnyRef =>
   *
   * Null => Null类是null引用对象的类型，是每个引用类(继承自AnyRef)的子类，不兼容值类型
   * Nothing => 是任何其它类型的子类型，其存在的一个用处是它标明了不正常的终止
   */

  /**
   * trait
   * 如果希望在java中被继承，则定义与抽象类，因为在java中继承特质是很笨拙的
   */

  /**
   * 隐式引用
   * 任何scala程序默认引入java.lang._; scala._; Predef._三个包，引入包时，出现在后面位置的引用将覆盖前面的引用
   */

  /**
   * 保护成员
   * scala比java更严格，在scala里，保护成员只在定义了成员的类的子类可以访问，而java中还允许在同一个包的其它类中进行访问
   * private[X]/protected[X]，表示”直到“X的私有或保护，这里X指代某个所属的包，类或单例对象
   * private[this] => 被这标记的定义仅能在包含了定义的同一个对象中被访问，此定义被称为是对象私有的(object-private)
   */

  /**
   * 类型推断是基于流的，在m(args)的方法调用中，类型推断器首先检查方法m是否有已知类型，有，那么这个类型将被用来做参数预期类型推断
   */

  /**
   * 所有能产生yield结果的for表达式都会被编译器转译为高阶方法map,filtermap及filter的组合调用
   * 所有不带yield的for循环都会被转译为仅对高阶函数filter和foreach的调用
   * for (seq) yield expr => seq是由生成器，定义及过滤器组成的序列，以分号分隔
   * demo:
   * for (p <- persons; n = p.name; if (n startsWith "To") ) yield n
   * 也可以这样:
   * for {
   * p <- persons;          ==> 生成器
   * n = p.name;            ==> 定义
   * if (n startWith "To")  ==> 过滤器
   * } yield n
   *
   * for (x <- List(1,2); y <- List("one","two")) yield (x,y)  => 结果是: List((1,"one"),(1,"two"),(2,"one"),(2,"two"))
   */

  object Email {
    def apply(user: String, domain: String) = user + "@" + domain

    /**
     * 抽取器
     * @param str
     * @return
     */
    def unapply(str: String): Option[(String, String)] = {
      val parts = str split "@"
      if (parts.length == 2) {
        val a = (parts(0), parts(1))
        Some(a)
      } else None
    }
  }

}