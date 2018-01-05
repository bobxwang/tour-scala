package com.bob.scalatour

object DbConfigs {

  object InTest {
    val banka = ("root", "****", "jdbc:mysql://ip:3306/dbname?useUnicode=true&characterEncoding=UTF-8")
  }

  object InPro {
    val usercert = ("root", "****", "jdbc:mysql://ip:port/dbname?useUnicode=true&characterEncoding=UTF-8")
  }

}