package com.bob.scalatour

import scalikejdbc._

case class Bank(id: Int, name: String)

object ScalikejdbcTips {

  /**
   * sbt "scalikejdbcGen T_Admin AdminEntity"
   * using this command to generate the class file according the table,T_Admin is a table name in db, will generate AdminEntity this class
   */

  Class.forName("com.mysql.jdbc.Driver")

  GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    printUnprocessedStackTrace = true,
    stackTraceDepth = 15,
    logLevel = 'debug,
    warningEnabled = false,
    warningThresholdMillis = 3000L,
    warningLogLevel = 'warn
  )

  ConnectionPool.singleton(DbConfigs.InTest.banka._3, DbConfigs.InTest.banka._1, DbConfigs.InTest.banka._2)

  def describe(table: String) = println(DB.describe(table))

  def tables() = println(DB.showTables())

  implicit val session = AutoSession

  def main(args: Array[String]) {
    try {
      DB.localTx(implicit session => {
        (1 to 5).foreach(x => {
          if (x > 3) {
            throw new Exception("just test for rollback")
          }
          //          val bb = BBTestEntity.create(Some(s"111${x}"), Some(s"eeee${x}"), Some(s"Ddd${x}"))
          //          println(bb)
          //          val am = AdminEntity.create(s"name${x}", "12345", 1, DateTime.now(), DateTime.now(), 1)
          //          println(am)
        })
      })
    } catch {
      case e: Exception => println(e.getMessage)
    }
  }

  def toMap(rs: WrappedResultSet): Map[String, Any] = {
    (1 to rs.metaData.getColumnCount).foldLeft(Map[String, Any]()) { (result, i) =>
      val label = rs.metaData.getColumnLabel(i)
      Some(rs.any(label)).map { nullableValue => {
        result + (label -> nullableValue)
      }
      }.getOrElse(result)
    }
  }

  def runInBank(): Unit = {

    val selfimp = sql"select BankID,BankName from T_Bank limit 2".map(rs => {
      toMap(rs)
    }).list.apply
    println(selfimp)

    // list results
    val names = sql"select * from T_Bank limit 10".map(rs => Bank(rs.int("BankID"), rs.string("BankName"))).list().apply()
    names.foreach(x => println(x))

    // single query
    val name = sql"select * from T_Bank where BankID = 2".map(rs => Bank(rs.int("BankID"), rs.string("BankName"))).single().apply();
    println(name)

    // first result from multiple results
    val bankid = sql"select BankID from T_Bank limit 10".map(rx => rx.int("BankID")).first().apply()
    println(bankid)

    // foreach allows you to make some side-effect in iterations. This API is useful for handling large ResultSet.
    sql"select BankName from T_Bank order by BankID desc limit 10".foreach(rx => println(rx.string("BankName")))

    // fetchSize not get five results from db,but caching the result if set 0 it maybe causes memory problems
    sql"select BankID from T_Bank order by BankID limit 1".fetchSize(5).foreach(rx => println(rx.int("BankID")))

    val bkid = 1
    val bank = sql"select BankID,BankName from T_Bank where BankID = ${bkid}"
      .map(rs => Bank(rs.int("BankID"), rs.string("BankName"))).single().apply()
    /*above code is equals belows, using SQLInterpolation technology to done this
    SQL("select BankID,BankName from T_Bank where BankID = {id}").bindByName('id -> bkid)
      .map(rs => Bank(rs.int("BankID"), rs.string("BankName"))).single.apply()
      */
    println(bank)

    val idsin = Seq(123, 124, 125)
    sql"SELECT * FROM T_Bank WHERE BankID IN ${idsin}"

    val isDesc = true
    val ordering = if (isDesc) sqls"desc" else sqls"asc"
    val ids = Seq(1, 3, 4)
    val aidbk = sql"SELECT * FROM T_Bank WHERE BankID IN (${ids}) order by BankID ${ordering}".map(r => r.string("BankName")).list().apply()
    println(aidbk)
  }
}