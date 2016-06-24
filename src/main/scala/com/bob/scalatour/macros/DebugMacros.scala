package com.bob.scalatour.macros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

object DebugMacros {

  /**
   * 打印只有一个入参的值
   * @param param
   */
  def debug(param: Any): Unit = macro debug_impl

  def debug_impl(c: Context)(param: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._
    val paramRep = show(param.tree)
    val paramRepTree = Literal(Constant(paramRep))
    val paramRepExpr = c.Expr[String](paramRepTree)
    reify {
      println(paramRepExpr.splice + " = " + param.splice)
    }
  }

  /**
   * 不止一个参数的宏，用此将会打印出所有参数名值，每个以逗号隔开
   * @param params
   */
  def debugm(params: Any*): Unit = macro debugm_impl

  def debugm_impl(c: Context)(params: c.Expr[Any]*): c.Expr[Unit] = {
    import c.universe._
    val trees = params.map(param => {
      param.tree match {
        case c.universe.Literal(c.universe.Constant(const)) => {
          val reified = reify {
            print(param.splice)
          }
          reified.tree
        }
        case _ => {
          val paramRep = show(param.tree)
          val paramRepTree = Literal(Constant(paramRep))
          val paramRepExpr = c.Expr[String](paramRepTree)
          val reified = reify {
            print(paramRepExpr.splice + " = " + param.splice)
          }
          reified.tree
        }
      }
    })
    // Inserting ", " between trees, and a println at the end.
    val separators = (1 to trees.size - 1).map(_ => (reify {
      print(", ")
    }).tree) :+ (reify {
      println()
    }).tree
    val treesWithSeparators = trees.zip(separators).flatMap(p => List(p._1, p._2))
    c.Expr[Unit](Block(treesWithSeparators.toList, Literal(Constant(()))))
  }

  def hello(): Unit = macro hello_impl

  def hello_impl(c: Context)(): c.Expr[Unit] = {
    import c.universe._
    c.Expr[Unit]( q"""println("Hello World")""")
  }
}