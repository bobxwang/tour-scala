package com.bob.scalatour

object EffectiveSacla {

  val name = null
  if (name == null) {

  }

  if (Option(name).isDefined) {
    // so we can avoid the null
  }

  val names = List("John", "Doc", "Stephen", "Doe")
  names.indices.map(x => println(x)) // x is the index


}
