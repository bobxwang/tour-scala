package com.bob.scalatour.utils

import scala.reflect.runtime.universe._

object TypeUtils {

  /**
   * 判断a是不是t的实例
   * @param a
   * @param t
   * @tparam A
   * @return ture if is match
   */
  def checkType[A: TypeTag](a: A, t: Type): Boolean = typeOf[A] <:< t


  
}