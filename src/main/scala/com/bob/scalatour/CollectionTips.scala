package com.bob.scalatour

/**
 * 集合操作一些提示和技巧
 */
object CollectionTips {

  def main(args: Array[String]) {
    val seq = Seq(1, 2, 3, 4, 5)
    println(isExist(seq, 8))
  }

  /**
   * 判断元素是否存在
   * @param seq
   * @param findValue
   * @return
   */
  def isExist(seq: Seq[Int], findValue: Int): Boolean = {
    println(seq.exists(_ == findValue))
    println(seq.contains(findValue))
    println(seq.filter(_ == findValue).headOption != None)
    println(seq.find(_ == findValue) != None)
    seq.find(_ == findValue).isDefined
  }
}