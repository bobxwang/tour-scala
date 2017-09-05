package com.bob.scalatour

import org.jsoup.Jsoup

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.util.parsing.json.JSON

/**
  * Created by wangxiang on 17/7/11.
  */
object WdaiTools {

  private val url = "http://portal.weidai.com.cn/login"
  val response = Jsoup.connect(url)
    .data("username", "wang.xiang")
    .data("password", "fuck51test")
    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
    .execute()
  implicit val cookie = response.cookies()

  def main(args: Array[String]): Unit = {
    val s ="""""".stripMargin.trim
    val head =
      """<thead>\s*<tr>\s*(?<Token>[\s\S]+?)\s*</tr>\s*</thead>""".stripMargin
    val body =
      """<tbody>\s*<tr>\s*(?<Token>[\s\S.]+?)\s*</tr>\s*</tbody>""".stripMargin

    //    val ssql = "select * from deduct_log where loan_no = '90101170609153100704862'"
    //    val l = connect(ssql, "collection")
    //    println(l)
    //    l.filter(x => {
    //      x.getOrElse("id", "0") == "535"
    //    })


//    var m = connect("select id,loan_no from weimi_task where staff_name = '谢素萍' order by id limit 50", "claudit")
//    do {
//      if (m.size > 0) {
//        var id = 0
//        m.foreach(x => {
//          val loan_no = x.get("loan_no").get
//          id = x.get("id").get.toInt
//          val l = connect(s"select case_id from case_overdue_record where loan_no = ${loan_no}", "collection")
//          if (l.size > 0) {
//            println(s"${loan_no} ---- ${l(0).get("case_id").get}")
//          } else {
//            println(s"${loan_no} ---- no case id")
//          }
//        })
//        m = connect(s"select id,loan_no from weimi_task where staff_name = '谢素萍' and id > ${id} order by id limit 50", "claudit")
//      }
//    } while (m.size > 0)

    sumwmd(10375,10388)
  }

  def sumsxd(y: Int, z: Int): Unit = {
    (y to z).foreach(x => {
      val l = connect(s"select loan_no from case_overdue_record where id = ${x}", "collection")
      if (l.size > 0) {
        val loan_no = l(0).get("loan_no").get
        val ll = connect(s"select audit_name from audit_allot where atid in (select id from audit_task where loan_no = '${loan_no}')", "claudit")
        if (ll.size > 0) {
          val audit_name = ll(0).get("audit_name").get
          println(s"${loan_no} --- ${audit_name}")
        }
      }
    })
  }

  def sumwmd(y: Int, z: Int): Unit = {
    (y to z).foreach(x => {
      val l = connect(s"select loan_no,customer_id from case_overdue_record where id = ${x}", "collection")
      if (l.size > 0) {
        val loan_no = l(0).get("loan_no").get
        val customer_id = l(0).get("customer_id").get
        val lll = connect(s"select loan_apply_time,loan_time,loan_money from loan_record where loan_no = '${loan_no}'","clmg")
        val loan_apply_time = lll(0).get("loan_apply_time").get
        val loan_time = lll(0).get("loan_time").get
        val loan_money = lll(0).get("loan_money").get
        val ll = connect(s"select staff_name from weimi_task where loan_no = '${loan_no}'", "claudit")
        if (ll.size > 0) {
          val audit_name = ll(0).get("staff_name").get
          val ls = connect(s"select name from customer_info where id = ${customer_id}","collection")
          val name = ls(0).get("name").get
          println(s"${loan_no} ---- ${audit_name} ---- ${name} --- applytime:${loan_apply_time} --- puttime:${loan_time} --- money:${loan_money}")
        } else {
          println(s"${loan_no} ---- no audit people --- applytime:${loan_apply_time} --- puttime:${loan_time} --- money:${loan_money}")
        }
      }
    })
  }

  def showtables(db: String)(implicit cookie: java.util.Map[String, String]): Unit = {
    val response = Jsoup.connect("http://webtool.weidai.com.cn/selectTables")
      .data("dataSourceType", db)
      .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
      .cookies(cookie)
      .header("Accept", "application/json, text/javascript, */*; q=0.01")
      .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
      .header("Referer", "http://webtool.weidai.com.cn/select")
      .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
      .header("Origin", "http://webtool.weidai.com.cn")
      .timeout(3000)
      .ignoreContentType(true)
      .execute().body()
    val json = JSON.parseFull(response)
    json match {
      case Some(m) => {
        val amap = m.asInstanceOf[Map[String, Any]]
        val tbs = amap.get("data").get.asInstanceOf[List[String]]
        println(tbs.mkString("\n"))
      }
      case None => println("null")
    }
  }

  def describe(tbname: String, db: String)(implicit cookie: java.util.Map[String, String]): Unit = {
    val response = Jsoup.connect("http://webtool.weidai.com.cn/selectTableDesc")
      .data("tableName", tbname)
      .data("dataSourceType", db)
      .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
      .cookies(cookie)
      .header("Accept", "application/json, text/javascript, */*; q=0.01")
      .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
      .header("Referer", "http://webtool.weidai.com.cn/select")
      .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
      .header("Origin", "http://webtool.weidai.com.cn")
      .timeout(3000)
      .ignoreContentType(true)
      .execute()
    val body = response.body()
    val json = JSON.parseFull(body)
    json match {
      case Some(m) => {
        val amap = m.asInstanceOf[Map[String, Any]]
        println(amap.getOrElse("data", ""))
      }
      case None => println("null")
    }
  }

  def connect(sql: String, db: String)(implicit cookie: java.util.Map[String, String]): List[Map[String, String]] = {
    val doc = Jsoup.connect("http://webtool.weidai.com.cn/select")
      .data("sql", sql)
      .data("dataSourceType", db)
      .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
      .cookies(cookie)
      .header("Accept", "text/html, */*; q=0.01")
      .header("Referer", "http://webtool.weidai.com.cn/select")
      .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
      .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
      .header("Origin", "http://webtool.weidai.com.cn")
      .timeout(3000)
      .post()
    val rs = analy(doc.toString)
    if (rs.isDefined) {
      rs.get
    } else {
      mutable.ListBuffer[Map[String, String]]().toList
    }
  }

  private def analy(rs: String): Option[List[Map[String, String]]] = {
    val doc = Jsoup.parse(rs)
    val ths = doc.getElementsByTag("th")
    val thsCount = ths.size()
    if (thsCount > 0) {
      val t = ths.asScala.map(x => {
        x.ownText()
      })
      val tds = doc.getElementsByTag("td")
      val aaa = tds.asScala.grouped(thsCount)
      val abc = aaa.map(f => {
        f.map(z => {
          z.ownText()
        })
      }).toList

      val ll = mutable.ListBuffer[Map[String, String]]()
      abc.foreach(x => {
        val user = mutable.Map[String, String]()
        for (i <- 0 until x.size) {
          user.put(t(i), x(i))
        }
        ll.append(user.toMap)
      })
      Some(ll.toList)
    } else {
      None
    }
  }
}