package com.bob.scalatour.extens

/**
 * 一系列类型扩展方法
 */
object TypeExtens {

  implicit class string2OtherType(val s: String) {

    /**
     * 支持能转成double类型的字条串
     * @return
     */
    def try2Int(): Option[Int] = {
      try {
        Some(java.lang.Double.valueOf(s).intValue())
      } catch {
        case e: Exception => None
      }
    }

    /**
     *
     * @return
     */
    def try2Long(): Option[Long] = {
      try {
        Some(s.toLong)
      } catch {
        case e: Exception => None
      }
    }
  }

  /**
   * 模拟linux中shell管道
   * @param t
   * @tparam T
   * @return
   */
  implicit def pipifunc[T](t: T) = new {
    def |[R](f: T => R): R = f(t)
  }

  /**
   * 传入一个函数，打印这个函数的执行时间
   * @param code
   * @tparam T
   * @return
   */
  def time[T](code: => T) = {
    val t0 = System.nanoTime: Double
    val res = code
    val t1 = System.nanoTime: Double
    println("Elapsed time " + (t1 - t0) / 1000000.0 + " msecs")
    res
  }

}
