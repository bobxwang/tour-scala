package com.bob.scalatour.designpattern

import com.bob.scalatour.designpattern.strategy.astrategy

object strategy {
  type astrategy = (Int, Int) => Int

  val add: astrategy = _ + _
  val mulitpy: astrategy = _ * _
}

class Context(computer: astrategy) {
  def use(a: Int, b: Int) = {
    computer(a, b)
  }
}

object Strategyrun {

  def main(args: Array[String]) {
    println(new Context(strategy.mulitpy).use(1, 2))
  }
}