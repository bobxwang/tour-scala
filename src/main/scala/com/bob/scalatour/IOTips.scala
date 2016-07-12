package com.bob.scalatour

import java.io._
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

import com.bob.scalatour.netflix.SRibbonClient
import com.bob.scalatour.utils.FileUtils
import okhttp3.OkHttpClient.Builder
import okhttp3.{OkHttpClient, Request => OKRequest}
import org.json4s._
import org.json4s.native.JsonMethods._
import com.bob.scalatour.extens.TypeExtens._

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

    //    Nothing.diffTwoFiles("lastuid","lastuid-ok-online","/Users/bob/Works/code/ScalaSolution/scalatour/src/main/resources/lastuid-not-c")

    //    fileContent("lastuid-ok-91-sort.txt").split(System.getProperty("line.separator", "\n")).toList.par.foreach(s => {
    //      val temp = s.split(",")
    //      val uid: Long = temp(0).try2Long.getOrElse(0)
    //      val anticheatlevel = temp(2).try2Int.getOrElse(0)
    //      val creditlevel = temp(4).try2Int.getOrElse(0)
    //      val ocredit = temp(8).try2Int.getOrElse(0)
    //      if (ocredit == 0) {
    //        if (anticheatlevel == 6 || creditlevel == 6) {
    //        } else {
    //          val date = temp(1)
    //          val anticheatno = temp(3)
    //          val creditno = temp(5)
    //          val ssalary = temp(6)
    //          val socredit = temp(7)
    //          val scredit = socredit
    //          val slimit = toLimit(scredit).getOrElse(0d)
    //          val c = s"${uid},${date},${anticheatlevel},${anticheatno},${creditlevel},${creditno},${ssalary},${socredit},${scredit},${slimit}"
    //          writeToFile(s"${currentPath}/needrunagain-in91-online.txt", s"${c}\n")
    //        }
    //      }
    //    })


    //    val oko1 = fileContent("o-order-ok-rerun-o").split(System.getProperty("line.separator", "\n")).toList.par.map(x => {
    //      val temp = x.split(",")
    //      (temp(0), temp(1), temp(2), temp(3))
    //    })
    //    val okaaa = fileContent("sorders").split(System.getProperty("line.separator", "\n")).toList.par.map(x => {
    //      val temp = x.split("\t")
    //      val time = temp(3).trim
    //      val uid = temp(0).trim
    //      val orderid = temp(1).trim
    //      val rs = temp(2).trim
    //      val times = time.split("/")
    //      val month = if (times(0).length == 1) s"0${times(0)}" else times(0)
    //      val day = if (times(1).length == 1) s"0${times(1)}" else times(1)
    //      val t = s"20${times(2)}-${month}-${day}"
    //      (orderid, rs, uid, t)
    //    })
    //    val diff = okaaa diff oko1
    //    val sb = diff.foldLeft[StringBuilder](StringBuilder.newBuilder) {
    //      (result, x) => {
    //        result.append(s"${x._1},${x._2},${x._3},${x._4}\n")
    //      }
    //    }
    //    writeToFile(s"${currentPath}/xx-reerr", sb.toString)


    //    val c = s"0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0"
    //
    //    val oko = fileContent("sorders-result").split(System.getProperty("line.separator", "\n")).toList.par.map(x => {
    //      val temp = x.split(",")
    //      (temp(0), temp(1), x)
    //    })
    //
    //    fileContent("xx-reerr").split(System.getProperty("line.separator", "\n")).toList.par.map(x => {
    //      val temp = x.split(",")
    //
    //      //      val uid = temp(0).trim
    //      //      val orderid = temp(1).trim
    //      //      val rs = temp(2).trim
    //      //
    //      //      val time = temp(3).trim
    //      //      val times = time.split("/")
    //      //      val month = if (times(0).length == 1) s"0${times(0)}" else times(0)
    //      //      val day = if (times(1).length == 1) s"0${times(1)}" else times(1)
    //      //      val t = s"20${times(2)}-${month}-${day}"
    //
    //      val uid = temp(2)
    //      val orderid = temp(0)
    //      val rs = temp(1)
    //      val t = temp(3)
    //      val tc = oko.find(x => {
    //        x._1 == uid && x._2 == t
    //      })
    //      tc match {
    //        case Some(v) => {
    //          val cc = s"${orderid},${rs},${v._3}\n"
    //          writeToFile(s"${currentPath}/o-order-ok-rerun", s"${cc}")
    //        }
    //        case None => {
    //          val cr = s"${orderid},${rs},${uid},${t},${c}\n"
    //          writeToFile(s"${currentPath}/o-order-ok-reerr", s"${cr}")
    //        }
    //      }
    //    })

    if (args.length != 2) {
      println("need two arguments,one is the file contains uid and date, other is a string to judge which url should to visit")
      return
    }

    //    val fo = fileContent("sorders").split(System.getProperty("line.separator", "\n")).toList.par.map(x => {
    //      val temp = x.split("\t")
    //      val time = temp(3).trim
    //      val uid = temp(0).trim
    //      val orderid = temp(1).trim
    //      val rs = temp(2).trim
    //      val times = time.split("/")
    //      val month = if (times(0).length == 1) s"0${times(0)}" else times(0)
    //      val day = if (times(1).length == 1) s"0${times(1)}" else times(1)
    //      val t = s"20${times(2)}-${month}-${day}"
    //      (uid, t, orderid, rs)
    //    })

    //    fileContent("sloaner").split(System.getProperty("line.separator", "\n")).toList.foreach(x => {
    //      try {
    //        val temp = x.split(",")
    //        val udate = temp(1).trim
    //        val uid = temp(0)
    //        val t = fo.find(x => {
    //          x._1 == uid && x._2 == udate
    //        })
    //        t match {
    //          case Some(v) => {
    //            val content = s"${x},${c}\n"
    //            writeToFile(s"${currentPath}/o-order-ok", s"${v._3},${v._4},${content}")
    //          }
    //          case None => {
    //            writeToFile(s"${currentPath}/o-error-91.txt", s"${x}\n")
    //          }
    //        }

    //        val crequest = requestBuild.url(s"http://172.16.40.91:9000/calculator/users/together?userId=${uid}&date=${udate}&bbb=bbb").build()
    //        val cresponse = client.newCall(crequest).execute()
    //        if (cresponse.isSuccessful) {
    //          val content = s"${x},${together2result(cresponse.body.string)}\n"
    //          val t = fo.find(x => {
    //            x._1 == temp(0) && x._2 == temp(1)
    //          })
    //          t match {
    //            case Some(v) => {
    //              writeToFile(s"${currentPath}/o-order-ok", s"${v._3},${v._4},${content}")
    //            }
    //            case None => {
    //              writeToFile(s"${currentPath}/o-error-91.txt", s"${x}\n")
    //            }
    //          }
    //        } else {
    //          writeToFile(s"${currentPath}/o-error-91.txt", s"${x} :: has error ${cresponse.code}\n")
    //        }
    //      } catch {
    //        case e: Exception => {
    //          writeToFile(s"${currentPath}/o-error-91.txt", s"${x} :: has error ${e.getMessage}\n")
    //        }
    //      }
    //    })

    val filename = args(0)
    val online = args(1)
    val fcontent = fileContent(filename)
    online match {
      case "online" => {
        // 访问线上
        fcontent.split(System.getProperty("line.separator", "\n")).toList.par.foreach(y => {
          try {
            val temp = y.trim.split(",")
            val udate = ""
            val uid = temp(0)
            val content = s"${uid},${together2result(SRibbonClient.run(uid, udate))}\n"
            writeToFile(s"${currentPath}/${filename}-ok-online.txt", content)
          } catch {
            case e: Exception => {
              writeToFile(s"${currentPath}/${filename}-error-online.txt", s"${y.trim} :: has error ${e.getMessage}\n")
            }
          }
        })
      }
      case _ => {
        // 访问91
        fcontent.split(System.getProperty("line.separator", "\n")).toList.par.foreach(y => {
          try {
            val temp = y.trim.split(",")
            val udate = temp(1).trim.split("-")
            val month = if (udate(1).length == 1) s"0${udate(1)}" else udate(1)
            val day = if (udate(2).length == 1) s"0${udate(2)}" else udate(2)
            val querydate = s"${udate(0)}-${month}-${day}"
            val uid = temp(0)
            val crequest = requestBuild.url(s"http://172.16.40.91:8090/calculator/users/together?userId=${uid}&date=${querydate}&bbb=bbb").build()
            val cresponse = client.newCall(crequest).execute()
            if (cresponse.isSuccessful) {
              val content = s"${y.trim},${together2result(cresponse.body.string)}\n"
              writeToFile(s"${currentPath}/${filename}-ok-91.txt", content)
            } else {
              writeToFile(s"${currentPath}/${filename}-error-91.txt", s"${y.trim} :: has error ${cresponse.code}\n")
            }
          } catch {
            case e: Exception => {
              writeToFile(s"${currentPath}/${filename}-error-91.txt", s"${y.trim} :: has error ${e.getMessage}\n")
            }
          }
        })
      }
    }


    //    val fContent = fileContent("20160421UID.txt")
    //    fContent.split(System.getProperty("line.separator", "\n")).toList.par.foreach(x => {
    //      val p = x.split(",")
    //      val uid = p(0).trim
    //      val date = p(1).replace("/", "-").trim
    //      try {
    //        val crequest = requestBuild.url(s"http://172.16.40.91:8090/calculator/users/credit?userId=${uid}&date=${date}&bbb=bbb").build();
    //        val cresponse = client.newCall(crequest).execute()
    //        //        val arequest = requestBuild.url(s"http://172.16.40.91:8090/calculator/users/anticheat?userId=${uid}&date=${date}&bbb=bbb").build();
    //        //        val aresponse = client.newCall(arequest).execute()
    //        if (cresponse.isSuccessful) {
    //          val credit = cresponse.body.string
    //          //          val anticheat = aresponse.body.string
    //          //          val javalue: JValue = parse(anticheat)
    //          //          val jalevel = (javalue \ "level").extract[String]
    //          //          val jano = (javalue \ "anticheat").extract[String]
    //          val jcvalue: JValue = parse(credit)
    //          val jclevel = (jcvalue \ "level").extract[String]
    //          val jcno = (jcvalue \ "credit").extract[String]
    //          val content = s"${uid},${date},${jcno},${jclevel}\n"
    //          writeToFile(s"${currentPath.getAbsolutePath}/ok_xufushu.txt", content)
    //        } else {
    //          var msg = cresponse.message
    //          //          if (msg == null) {
    //          //            msg = aresponse.message
    //          //          }
    //          if (msg == null) {
    //            msg = " no reaseon"
    //          }
    //          writeToFile(s"${currentPath.getAbsolutePath}/error.txt", s"uidis ${uid} has error ${msg} \n")
    //        }
    //      } catch {
    //        case e: Exception => {
    //          writeToFile(s"${currentPath.getAbsolutePath}/error.txt", s"uidis ${uid} has error ${e.getMessage}\n")
    //        }
    //      }
    //    })


    //    val tempsb = StringBuilder.newBuilder
    //    tempsb.append("userid,date,antino,antilevel,creditno,creditlevel")
    //    val sb = fContent.split(System.getProperty("line.separator"))
    //      .foldLeft[StringBuilder](tempsb) {
    //      (result, x) => {
    //        try {
    //          val p = x.split(",")
    //          val uid = p(0).trim
    //          val date = p(1).replace("/", "-").trim
    //          val credit = Source.fromURL(s"http://172.16.40.91:8090/calculator/users/credit?userId=${uid}&date=${date}").mkString
    //          val anticheat = Source.fromURL(s"http://172.16.40.91:8090/calculator/users/anticheat?userId=${uid}&date=${date}").mkString
    //          val javalue: JValue = parse(anticheat)
    //          val jalevel = (javalue \ "level").extract[String]
    //          val jano = (javalue \ "anticheat").extract[String]
    //          val jcvalue: JValue = parse(credit)
    //          val jclevel = (jcvalue \ "level").extract[String]
    //          val jcno = (jcvalue \ "credit").extract[String]
    //          result.append(s"${uid},${date},${jano},${jalevel},${jcno},${jclevel}")
    //        } catch {
    //          case e: Exception => {
    //            e.printStackTrace
    //            result
    //          }
    //        }
    //      }
    //    }
    //    writeToFile("/Users/bob/Desktop/xufushu.txt", sb.toString())
    //    println(sb)
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