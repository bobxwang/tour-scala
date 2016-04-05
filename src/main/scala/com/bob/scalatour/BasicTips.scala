package com.bob.scalatour

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
  }

}