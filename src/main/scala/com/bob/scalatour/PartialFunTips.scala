package com.bob.scalatour

/**
 * 一个PartialFunction[A, B]类型的函数是一个一元函数，接收一个类型为A的参数，返回类型为B的值。
 * 但是X的值域可以不覆盖A的整个值域，只覆盖部分的值域。
 * 其中isDefinedAt可以测试是否一个值是否落在了定义的参数值域上。
 * 白话来讲就是PartialFunction只处理参数的一个子集。
 */
object PartialFunTips {

  def main(args: Array[String]) {
    val isEven: PartialFunction[Int, String] = {
      case x if x % 2 == 0 => s"${x} is even"
    }

    val isOdd: PartialFunction[Int, String] = {
      case x if x % 2 == 1 => s"${x} is odd"
    }

    val sample = 1 to 10

    /**
     * collect 可以理解为先filter再map，直接操作一次循环搞定
     */
    val evenNumbers = sample collect isEven

    val numbers = sample map (isEven orElse isOdd)

    println(evenNumbers)

    println(numbers)

    println(sample.map(x => isEven.applyOrElse(x, isOdd)))

  }
}