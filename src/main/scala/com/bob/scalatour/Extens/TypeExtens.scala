package com.bob.scalatour.Extens

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

}