package com.bob.scalatour.configs

import com.typesafe.config._
import org.scalatest.FunSuite

class ConfigTest extends FunSuite {
  test("load config") {
    val config = ConfigFactory.load("config.conf")
    assert(config.getString("app.name") == "scalatour")
    assert(config.getString("app.db.url") == "jdbc:h2:mem:test;INIT=bbtest FROM 'classpath:ddl.sql")
    assert(config.getString("app.db.driver") == "org.h2.Driver")
    assert(config.getString("app.http.host") == "0.0.0.0")
    assert(config.getInt("app.http.port") == 9999)
  }
}