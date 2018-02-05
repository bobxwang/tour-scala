package com.bob.scalatour

/**
  * 路径依赖类型
  */
class Franchise(name: String) {

  // 这样类型Character嵌套在Franchise里, 它依赖于一个特定的Franchise实例
  case class Character(name: String)

  // 参数类型依赖于传递给该方法的 Franchise 实例
  def createFanFictionWith(lovestruck: Character,
                           objectOfDesire: Character): (Character, Character) =
    (lovestruck, objectOfDesire)
}

object PathDependency extends App {

  val starTrek = new Franchise("Star Trek")
  val starWars = new Franchise("Star Wars")

  val quark = starTrek.Character("Quark")
  val jadzia = starTrek.Character("Jadzia Dax")

  val luke = starWars.Character("Luke Skywalker")
  val yoda = starWars.Character("Yoda")

  starTrek.createFanFictionWith(lovestruck = quark, objectOfDesire = jadzia)
  starWars.createFanFictionWith(lovestruck = luke, objectOfDesire = yoda)
  // 上面两句会顺利编译通过
  // 下面这句将不会通过,因为它将不会通过,编译器会抱怨类型不匹配
  // starTrek.createFanFictionWith(lovestruck = jadzia, objectOfDesire = luke)
}