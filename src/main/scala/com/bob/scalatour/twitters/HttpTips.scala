package com.bob.scalatour.twitters

import com.twitter.finagle.transport.Transport
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Method, Response, Request}
import com.twitter.finagle.loadbalancer.{Balancers, LoadBalancerFactory}
import com.twitter.util.{Await => twitterAwait}

object HttpTips {

  def main(args: Array[String]) {
    val balancer: LoadBalancerFactory = Balancers.heap()

    val client: Service[Request, Response] = Http.client
      .withLabel("my-http-client")
      .withLoadBalancer(balancer)
      .configured(Transport.Options(noDelay = false, reuseAddr = false))
      .newService("riskopenapi.51.nb:8091,riskopenapi.51.nb:8090")

    val url = Request.queryString("/calculator/users/anticheat", Map("userId" -> "314"))
    println(url)
    val request = Request(Method.Get, "/")
    request.headerMap.add("host", "riskopenapi.51.nb")
    (1 to 20).map {
      z => {
        println(s"now is the ${z} request to send")
        val response = client(request)
        twitterAwait.ready(response.map(x => println(x.contentString)))
      }
    }
    println("http invoke done")
  }
}