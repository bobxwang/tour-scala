package com.bob.scalatour

import java.io._
import java.text.SimpleDateFormat
import java.util.Scanner

import com.google.common.io.CharStreams
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.io.Source

/**
 * Created by bob on 16/4/13.
 */
object IOTips {

  implicit def writeToFile(fileName: String, data: String) = FileUtils.writeToFile(fileName, data)

  implicit def fileContent(fileName: String): String = FileUtils.fileContent(fileName)

  def main(args: Array[String]) {
    val fContent = fileContent("cityinfo.txt")
    val p = """ cardArr\["(.+?)"\]="(.+?)" """.trim.r
    val sb = fContent.split(";").map(x => {
      val p(abcd, efg) = x
      (abcd, efg)
    }).foldLeft(StringBuilder.newBuilder) {
      (result, x) => result.append(System.getProperty("line.separator", "\n")).append(x._1 + "=" + x._2)
    }
    println(sb.toString)
  }

  def whitename(): Unit = {
    val rpfcontent = fileContent("whitename.txt")
    val result = rpfcontent.split(System.getProperty("line.separator"))
      .foldLeft[StringBuilder](StringBuilder.newBuilder) {
      (result, x) => {
        val p = x.split(",")
        val sql = s"insert into T_White_Loan (mobile,username,remark) values ('${p(2).trim}','${p(1).trim}','${p(0).trim}');\n"
        result.append(sql)
      }
    }
    writeToFile("/Users/bob/Desktop/sql_2_dba.txt", result.toString())
    println(result)
  }

  def rpffq() = {
    implicit val formats: Formats = new DefaultFormats {
      override def dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }

    val rpfcontent = fileContent("rpfuserid.txt")
    val result = rpfcontent.split(System.getProperty("line.separator"))
      .foldLeft[StringBuilder](StringBuilder.newBuilder) {
      (result, x) => {
        try {
          val p = x.split( """\t""".stripMargin)
          val credit = Source.fromURL(s"http://172.16.40.68:8090/calculator/users/credit?userId=${p(0).trim}&procode=11").mkString
          val antichat = Source.fromURL(s"http://172.16.40.68:8090/calculator/users/anticheat?userId=${p(0).trim}&procode=11").mkString
          val salary = Source.fromURL(s"http://172.16.40.68:8090/calculator/users/salary?userId=${p(0).trim}&cityname=北京&procode=10").mkString
          val javalue: JValue = parse(antichat)
          val jalevel = (javalue \ "level").extract[String]
          val jcvalue: JValue = parse(credit)
          val jclevel = (jcvalue \ "level").extract[String]
          val jsvalue: JValue = parse(salary)
          val jsgroupc = (jsvalue \ "groupocredit").extract[String]
          val jsgroups = (jsvalue \ "groupsalary").extract[String]
          val ss = s"${p(0)},${p(1)},${p(2)},${p(3)},${p(4)},${jalevel},${jclevel},${jsgroupc},${jsgroups}\n"
          result.append(ss)
        } catch {
          case e: Exception => {
            e.printStackTrace
            result
          }
        }
      }
    }
    writeToFile("/Users/bob/Desktop/xuuser.csv", result.toString())
    println(result.toString)
  }
}


object FileUtils {

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
    using(new FileWriter(fileName)) {
      fileWriter => fileWriter.write(data)
    }
}