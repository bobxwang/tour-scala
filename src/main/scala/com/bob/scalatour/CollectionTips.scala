package com.bob.scalatour

/**
 * 集合操作一些提示和技巧
 */
object CollectionTips {

  /**
   * 优先使用length而不是size获取数组长度
   * 不要通过计算length来检查empty，使用seq.nonEmpty或seq.isEmpty
   * 不直接使用length来比较，使用seq.lengthCompare(n) < 13
   * 不直接使用==比较数组，采用array1.sameElements(array2)
   * 不要显示检查index的边界，采用seq.lift(i)，返回Option
   * 不构造index的Range，Range(0,seq.length)，采用seq.indices
   * 不借助filter统计元素数量，seq.filter(p).length，采用seq.count(p)
   * 合并连续的map调用，seq.map(f).map(g) => seq.map(f.andThen(g))
   * 使用isDefined来检查值是否存在，而不是通过模式匹配
   */

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