package com.bob.scalatour.utils

import java.io._
import java.util.Scanner

import com.google.common.io.CharStreams

object FileUtils {

  /**
   * 列出此文件下的所有满足条件的文件名
   * @param dir
   * @param filter
   * @return
   */
  def findAllConditionFiles(dir: File)(filter: File => Boolean): List[String] = {
    var result: List[String] = Nil
    def find(file: File): Unit = file match {
      case dir if file.isDirectory => dir.listFiles.foreach(x => find(x))
      case f if filter(file) => result = f.getName :: result
      case _ =>
    }
    find(dir)
    result
  }

  def fileContent(fileName: String): String = {
    val url = Thread.currentThread.getContextClassLoader.getResource(fileName)
    /*
     * 这代码在idea中可以跑，但打包成jar包后不行，因为文件已经被打进jar包内，读取方式变了
     * val fileContent = Source.fromFile(url.getFile)(Codec.UTF8).mkString
     * println(fileContent)
     */

    // 以下方案在idea或者java -jar jarname中都可以跑，Scanner是jdk1.5后所新增的一个类，从inputstream读出字符串
    val inputStream = url.openStream()
    usingGuava(inputStream)
  }

  def usingBufferread(inputStream: InputStream): String = {
    val bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
    var firstLine = true
    val sb = new StringBuilder()
    var line = bufferedReader.readLine()
    while (line != null) {
      if (!firstLine) {
        sb.append(System.getProperty("line.separator"))
      } else {
        firstLine = false
      }
      sb.append(line)
      line = bufferedReader.readLine()
    }
    sb.toString
  }

  /**
   * 利用guava库从inputstream读取
   * @param inputStream
   * @return
   */
  def usingGuava(inputStream: InputStream): String = {
    val content = CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"))
    inputStream.close()
    content
  }

  /**
   * jdk1.5后新增Scanner读取
   * @param inputStream
   * @return
   */
  def usingScanner(inputStream: InputStream): String = {
    using(new Scanner(inputStream)) {
      scanner => {
        val fileContent = scanner.useDelimiter("\\A").next()
        fileContent
      }
    }
  }

  def using[A <: {def close() : Unit}, B](param: A)(f: A => B): B =
    try {
      f(param)
    } finally {
      param.close()
    }

  def writeToFile(fileName: String, data: String) =
    using(new FileWriter(fileName, true)) {
      fileWriter => {
        fileWriter.append(data)
      }
    }
}
