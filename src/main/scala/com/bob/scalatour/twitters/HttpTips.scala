package com.bob.scalatour.twitters

import com.twitter.finagle.transport.Transport
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Method, Response, Request}
import com.twitter.finagle.loadbalancer.{Balancers, LoadBalancerFactory}
import com.twitter.util.{Await => twitterAwait, Monitor}

object HttpTips {

  def main(args: Array[String]) {
    val balancer: LoadBalancerFactory = Balancers.heap()

    val monitor: Monitor = new Monitor {
      override def handle(exc: Throwable): Boolean = {
        // do sth with the exception
        true
      }
    }

    val client: Service[Request, Response] = Http.client
      .withMonitor(monitor)
      .withLoadBalancer(balancer)
      // It’s important to disable Fail Fast when only have one host in the replica set
      // because Finagle doesn’t have any other path to choose.
      .withSessionQualifier.noFailFast
      .configured(Transport.Options(noDelay = false, reuseAddr = false))
      .newService("172.16.40.68:8090,172.16.40.69:8090", "riskservice")

    val request = Request(Method.Get, "/calculator/users/together?userId=1234&date=2015-12-12&bbb=bbb")
    // here we should set the host header value even it is empty,otherwise it will nothing can obtain
    request.headerMap.add("host", "")
    (1 to 20).map {
      z => {
        println(s"now is the ${z} request to send")
        val response = client(request)
        twitterAwait.ready(response.map(x => println(x.contentString)))
      }
    }

    val url = Request.queryString("/calculator/users/together", Map("userId" -> "314", "date" -> "2015", "bbb" -> "bbb"))
    println(url) // url value will be: /calculator/users/together?userId=314&date=2015&bbb=bbb
    println("http invoke done")
  }
}