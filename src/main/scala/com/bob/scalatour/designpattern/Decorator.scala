package com.bob.scalatour.designpattern

trait OutputStream {
  def write(b: Byte)

  def write(b: Array[Byte])
}

class FileOutputStream(path: String) extends OutputStream {
  override def write(b: Byte): Unit = {
    println(b.toString)
  }

  override def write(b: Array[Byte]): Unit = ???
}

trait Buffering extends OutputStream {
  abstract override def write(b: Byte): Unit = {
    // do sth
    println("now in buffering trait")
    super.write(b)
  }
}

/**
 * 装饰模式被用来在不影响一个类其它实例的基础上扩展一些对象的功能，是对继承的一个灵活替代
 */
object Decorator {
  def main(args: Array[String]) {

    val fops = new FileOutputStream("") with Buffering
    fops.write(1 toByte)
  }
}
