package com.bob.scalatour.utils

import java.text.SimpleDateFormat

import org.json4s._
import org.json4s.native.JsonMethods._

import scala.io.Source

object Nothing {

  implicit def writeToFile(fileName: String, data: String) = FileUtils.writeToFile(fileName, data)

  implicit def fileContent(fileName: String): String = FileUtils.fileContent(fileName)

  implicit val formats: Formats = new DefaultFormats {
    override def dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  }

  def usingSourceIO(x: String) = {
    val p = x.split(",")
    val uid = p(0).trim
    try {
      val date = p(1).replace("/", "-").trim
      val credit = Source.fromURL(s"http://172.16.40.91:8090/calculator/users/credit?userId=${uid}&date=${date}&bbb=bbb").mkString
      val anticheat = Source.fromURL(s"http://172.16.40.91:8090/calculator/users/anticheat?userId=${uid}&date=${date}&bbb=bbb").mkString
      val javalue: JValue = parse(anticheat)
      val jalevel = (javalue \ "level").extract[String]
      val jano = (javalue \ "anticheat").extract[String]
      val jcvalue: JValue = parse(credit)
      val jclevel = (jcvalue \ "level").extract[String]
      val jcno = (jcvalue \ "credit").extract[String]
      val content = s"${uid},${date},${jano},${jalevel},${jcno},${jclevel}\n"
      writeToFile("/Users/bob/Desktop/xufushu1.txt", content)
    } catch {
      case e: Exception => {
        writeToFile("/Users/bob/Desktop/error.txt", s"uidis ${uid} has error ${e.getMessage}\n")
      }
    }
  }

  def cityinfo(): Unit = {
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
    val rpfcontent = fileContent("rpfuserid.txt")
    val result = rpfcontent.split(System.getProperty("line.separator"))
      .foldLeft[StringBuilder](StringBuilder.newBuilder) {
      (result, x) => {
        try {
          val p = x.split( """ \ t""".stripMargin)
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