package com.bob.scalatour

import java.util.UUID

import org.bouncycastle.util.encoders.{DecoderException, Hex => BHex}
import org.apache.commons.codec.binary.{Hex => CHex}

/**
  * 适配mysql数据库的binary类型做主键策略
  * Created by wangxiang on 17/9/21.
  */
object PKGenerator {

  def genString: String = {
    val strUUID = UUID.randomUUID.toString
    val strbPkUUID = new StringBuilder
    strbPkUUID.append(strUUID.substring(14, 18))
    strbPkUUID.append(strUUID.substring(9, 13))
    strbPkUUID.append(strUUID.substring(0, 8))
    strbPkUUID.append(strUUID.substring(19, 23))
    strbPkUUID.append(strUUID.substring(24))
    strbPkUUID.toString.toUpperCase
  }

  def genByte: Array[Byte] = {
    BHex.decode(genString)
  }

  /**
    * 模拟mysql数据库中的hex函数　
    *
    * @param bytes
    * @return
    */
  def hex(bytes: Array[Byte]): String = CHex.encodeHexString(bytes).toUpperCase

  /**
    * 模拟mysql数据库中的unhex函数　
    *
    * @param pkString
    * @throws
    * @return
    */
  @throws[DecoderException]
  def unhex(pkString: String): Array[Byte] = BHex.decode(pkString)
}