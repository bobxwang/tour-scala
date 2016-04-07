package com.bob.scalatour.designpattern

trait Animal

private class Dog extends Animal

private class Cat extends Animal

/**
 * 工厂方法被定义为伴生对象，是一种特殊的单例对象，此语法仅限于工厂模式中的静态工厂模式
 */
object Animal {

  def apply(kind: String) = kind match {
    case "dog" => new Dog
    case "cat" => new Cat
  }
}