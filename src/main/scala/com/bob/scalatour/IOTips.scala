package com.bob.scalatour

import java.io._
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

import com.bob.scalatour.extens.TypeExtens._
import com.bob.scalatour.netflix.SRibbonClient
import com.bob.scalatour.utils.FileUtils
import okhttp3.OkHttpClient.Builder
import okhttp3.{OkHttpClient, Request => OKRequest}
import org.json4s._
import org.json4s.native.JsonMethods._

/**
 * Created by bob on 16/4/13.
 */
object IOTips {

  implicit val formats: Formats = new DefaultFormats {
    override def dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  }

  val client: OkHttpClient = new Builder()
    .connectTimeout(5, TimeUnit.SECONDS)
    .writeTimeout(5, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .build()
  val requestBuild = new OKRequest.Builder()

  def toLimit(s: String): Option[Double] = {
    try {
      val initlimit = s.try2Int().getOrElse(0)
      if (initlimit < 0) {
        Some(0d)
      } else if (initlimit > 100000) {
        Some(100000d)
      } else {
        val ii = (initlimit / 500) * 500
        Some(ii)
      }
    } catch {
      case e: Exception => None
    }
  }

  def main(args: Array[String]) {

    val a = new File("")
    val currentPath = a.getAbsolutePath
    println(currentPath)

    if (args.length != 2) {
      println("need two arguments,one is the file contains uid and date, other is a string to judge which url should to visit")
      return
    }

    val filename = args(0)
    val online = args(1)
    val fcontent = fileContent(filename)
    online match {
      case "online" => {
        // 访问线上
        fcontent.split(System.getProperty("line.separator", "\n")).toList.par.foreach(y => {
          try {
            val temp = y.trim.split(",")
            val udate = temp(1)
            val uid = temp(0)
            val content = s"${y.trim},${together2result(SRibbonClient.run(uid, udate))}\n"
            writeToFile(s"${currentPath}/${filename}-ok-online.txt", content)
          } catch {
            case e: Exception => {
              writeToFile(s"${currentPath}/${filename}-error-online.txt", s"${y.trim} :: has error ${e.getMessage}\n")
            }
          }
        })
      }
      case _ => {
        fcontent.split(System.getProperty("line.separator", "\n")).toList.foreach(y => {
          try {
            val temp = y.trim.split(",")
            val uid = temp(0)
            val date = temp(1)
            val crequest = requestBuild.url(s"http://*****/****/***/***?userId=${uid}&date=${date}&bbb=bbb").build()
            val cresponse = client.newCall(crequest).execute()
            if (cresponse.isSuccessful) {
              val jv: JValue = parse(cresponse.body().string())
              val content = s"${y.trim},${(jv \ "agent").extractOrElse[Double](-1000.0d)}\n"
              writeToFile(s"${currentPath}/${filename}-ok-91.txt", content)
            } else {
              writeToFile(s"${currentPath}/${filename}-error-91.txt", s"${y.trim} :: has error ${cresponse.code}\n")
            }
          } catch {
            case e: Exception => writeToFile(s"${currentPath}/${filename}-error-91.txt", s"${y.trim} :: has error ${e.getMessage}\n")
          }
        })
      }
    }
  }

  /**
   * 将访问together接口获取欺诈，信用，薪水三个数值进行字符串化
   * @param rs
   * @return
   */
  def together2result(rs: String): String = {
    val jv: JValue = parse(rs)
    val anticheat = jv \ "left";
    val credit = jv \ "middle";
    val salary = jv \ "right";
    val anticheatlevel = (anticheat \ "level").extract[Double]
    val anticheatno = (anticheat \ "anticheat").extract[Double]
    val creditlevel = (credit \ "level").extract[Double]
    val creditno = (credit \ "credit").extract[Double]
    val ssalary = (salary \ "salary").extract[Double]
    val socredit = (salary \ "ocredit").extract[Double]
    val scredit = (salary \ "credit").extract[Double]
    val slimit = (salary \ "limit").extract[Double]
    s"${anticheatlevel},${anticheatno},${creditlevel},${creditno},${ssalary},${socredit},${scredit},${slimit}"
  }

  def writeToFile(fileName: String, data: String) = FileUtils.writeToFile(fileName, data)

  def fileContent(fileName: String): String = FileUtils.fileContent(fileName)

}
