package com.bob.scalatour.macros

import scala.reflect.macros.whitebox.Context

import scala.language.experimental.macros

case class Location(filename: String, line: Int, column: Int)

object Location {

  implicit def capture: Location = macro Location.locationMacro

  def locationMacro(x: Context): x.Expr[Location] = {
    import x.universe._

    val pos = x.macroApplication.pos
    val clsLocation = x.mirror.staticModule("Location") // get symbol of "Location" object

    //    typeOf[Location].termSymbol
    // using these to avoid manually lookup symbols
    //    typeOf[Location.type].termSymbol

    x.Expr(Apply(Ident(clsLocation), List(Literal(Constant(pos.source.path)), Literal(Constant(pos.line)), Literal(Constant(pos.column)))))
  }
}