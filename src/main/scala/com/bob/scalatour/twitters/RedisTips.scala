package com.bob.scalatour.twitters

import com.twitter.finagle.Redis

object RedisTips {

  val redisHosts = List("192.168.2.213:7000", "192.168.2.213:7001", "192.168.2.213:7002", "192.168.2.213:7003", "192.168.2.213:7004", "192.168.2.213:7005")

  implicit val redisClients = redisHosts.map(x => {
    Redis.client.newRichClient(x)
  })

  def main(args: Array[String]): Unit = {

  }
}