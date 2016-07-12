package com.bob.scalatour

object DbConfigs {

  object InTest {
    val banka = ("root", "zufangbao69fc", "jdbc:mysql://192.168.2.200:3306/51banka?useUnicode=true&characterEncoding=UTF-8")
  }

  object InPro {
    val usercert = ("usercert", "KPUA0YkVNxTLhGVM", "jdbc:mysql://m.usercert.51.nb:3310/usercert?useUnicode=true&characterEncoding=UTF-8")
  }

}