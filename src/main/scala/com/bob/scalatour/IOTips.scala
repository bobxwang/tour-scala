package com.bob.scalatour

import java.io.FileWriter

import scala.io.{Codec, Source}

/**
 * Created by bob on 16/4/13.
 */
object IOTips {

  def main(args: Array[String]) {

    operation("/Users/bob/Desktop/whitename.txt")
  }

  /**
   * 从文件路径系统中读
   * @param path
   */
  def operation(path: String): Unit = {

    val source = Source.fromFile(path)(Codec.UTF8).mkString
    val rs = source.split(System.getProperty("line.separator", "\n")).map(x => {
      val p = x.split(";")
      (p(0).trim, p(1).trim)
    }).foldLeft(StringBuilder.newBuilder) {
      (result, x) => result.append(System.getProperty("line.separator", "\n")).append(x._1 + " " + x._2)
    }
    println(rs)

    writeToFile("/Users/bob/Desktop/white-fg-db.txt", rs.toString)
  }

  def using[A <: {def close() : Unit}, B](param: A)(f: A => B): B =
    try {
      f(param)
    } finally {
      param.close()
    }

  def writeToFile(fileName: String, data: String) =
    using(new FileWriter(fileName)) {
      fileWriter => fileWriter.write(data)
    }
}