package com.bob.scalatour.twitters

import java.sql.Timestamp
import java.util.{Date, TimeZone}

import com.twitter.finagle.exp.Mysql
import com.twitter.finagle.exp.mysql.TimestampValue
import com.twitter.util.{Await => twitterAwait}

object MysqlTips {

  def main(args: Array[String]) {
    val mysqlClient = Mysql.client
      .withCredentials("root", "zufangbao69fc")
      .withDatabase("51banka")
      .newRichClient("192.168.2.200:3306")

    val query = "select * from T_BKProcess where BankID = 5 limit 5"
    val resultq = mysqlClient.select(query) {
      row => {
        val ct = row("CreateTime").get
        (ct, row("IdCard").get)
      }
    }.map(x => {
      x.foreach(println)
    }).ensure(mysqlClient.close)
    twitterAwait.result(resultq)

    val insertSql = """ INSERT INTO `51banka`.`afenqi`
                      |(`UserId`,
                      |`Credit`,
                      |`Creditlevel`,
                      |`CreateTime`)
                      |VALUES
                      |(?,?,?,?); """.stripMargin.replaceAll("\n", "")
    val ps = mysqlClient.prepare(insertSql)
    val timezone = new TimestampValue(TimeZone.getDefault, TimeZone.getDefault)
    twitterAwait.result(ps(12, 1, 2, timezone.apply(new Timestamp(new Date().getTime))).ensure(mysqlClient.close))

    println("invoke done")
  }
}