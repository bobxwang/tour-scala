package com.bob.scalatour.scalazsss

import java.sql.Connection

import org.apache.commons.dbcp2.BasicDataSource

import scalaz.Scalaz._
import scalaz.{Free, Functor, \/}

object FreeInscalaz {

  type Sql[A] = Free[SqlOp, A]
  implicit val sqlOpFunctor = new Functor[SqlOp] {
    def map[A, B](sa: SqlOp[A])(f: A => B): SqlOp[B] = SqlOp { (conn: Connection) => f(sa.run(conn)) }
  }

  /**
   * 修改DB,可以理解成mapper或dao层
   * @param autoId
   * @param bankName
   * @return
   */
  def updateName(autoId: Int, bankName: String): Sql[Int] =
    Free.liftF(SqlOp {
      conn => {
        val sql = s"update T_BkBank set BankName = '${bankName}' where AutoID = ${autoId};"
        conn.createStatement.executeUpdate(sql)
      }
    })

  /**
   * 将上面几个方法自由组合,可以理解成service做的事情
   * @param autoId
   * @return
   */
  def conposeit(autoId: Int): Sql[Unit] = for {
    findId <- getAutoId(autoId)
    _ <- updateStatus(findId, 100)
    _ <- updateName(findId, "fuck51")
  } yield ()

  def getAutoId(autoId: Int): Sql[Int] =
    Free.liftF(SqlOp {
      (conn: Connection) => {
        val sql = s"select AutoID from T_BkBank where AutoID = ${autoId};"
        val rs = conn.createStatement().executeQuery(sql)
        var result = 0
        while (rs.next) {
          result = rs.getInt(1)
        }
        result
      }
    })

  def updateStatus(autoId: Int, status: Int): Sql[Int] =
    Free.liftF(SqlOp {
      conn => {
        val sql = s"update T_BkBank set Status = ${status} where AutoID = ${autoId};"
        conn.createStatement.executeUpdate(sql)
      }
    })

  def runTransactionImpl[A](conn: Connection, ast: Sql[A]): A =
    ast.resume.fold({
      case x: SqlOp[Sql[A]] => runTransactionImpl(conn, x.run(conn))
    }, (a: A) => a)

  def runTransaction[A](ast: Sql[A]): Exception \/ A = {
    val ds = new BasicDataSource()
    ds.setUrl("jdbc:mysql://192.168.2.200:3306/51banka?useUnicode=true&characterEncoding=UTF-8")
    ds.setUsername("root")
    ds.setPassword("zufangbao69fc")
    ds.setDefaultAutoCommit(false)
    val conn = ds.getConnection
    conn.rollback()
    try {
      val result: A = runTransactionImpl(conn, ast)
      conn.commit
      result.right[Exception]
    } catch {
      case e: Exception => {
        conn.rollback
        e.left[A]
      }
    } finally {
      conn.close
    }
  }

  def main(args: Array[String]) {
    println(runTransaction(conposeit(47)))
  }

  case class SqlOp[A](run: Connection => A)

}