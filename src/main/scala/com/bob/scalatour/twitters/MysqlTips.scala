package com.bob.scalatour.twitters

import java.sql.Timestamp
import java.util.{Date, TimeZone}

import com.twitter.util.{Await => twitterAwait}
import com.twitter.finagle.client._
import com.twitter.conversions.time._
import com.twitter.finagle.Mysql
import com.twitter.finagle.mysql.TimestampValue

object MysqlTips {

  def main(args: Array[String]) {

    this.sqlClient()

    val mysqlClient = Mysql.client
      .withCredentials("root", "password")
      .withDatabase("dbname")
      .configured(DefaultPool.Param(low = 0, high = 10,
        idleTime = 5.minutes,
        bufferSize = 0,
        maxWaiters = Int.MaxValue))
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

    val insertSql =
      """ INSERT INTO `scheme`.`dbname`
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

  def sqlClient() = {
    val mysqlClient = Mysql.client
      .withCredentials("username", "password")
      .withDatabase("dbname")
      .newRichClient("192.168.59.135:3310")
    val query = "select * from bank_account limit 10"
    mysqlClient.select(query) {
      row => {
        val ct = row("account").get
        (ct, row("name").get)
      }
    }.map(x => {
      x.foreach(println)
    }).ensure(mysqlClient.close)

  }
}