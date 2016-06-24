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

  def getType[T: TypeTag](obj: T) = typeOf[T]

  /**
   * T是不是S的子类
   * @param x
   * @param y
   * @tparam T
   * @tparam S
   * @return
   */
  def m[T: TypeTag, S: TypeTag](x: T, y: S): Boolean = {
    val leftTag = typeTag[T]
    val rightTag = typeTag[S]
    leftTag.tpe <:< rightTag.tpe
  }

  def main(args: Array[String]) {
    checkType((1, 2), typeOf[Tuple2[String, String]])
  }


}