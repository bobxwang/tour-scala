package com.bob.scalatour.netflix

import java.text.SimpleDateFormat

import com.netflix.config.ConfigurationManager._
import feign.codec.ErrorDecoder
import feign.ribbon.LoadBalancingTarget
import feign._
import org.json4s._
import org.json4s.native.JsonMethods._

object SRibbonClient {

  private def client: String = {
    return "risk"
  }

  private trait Risk {
    @RequestLine("GET /calculator/users/together?userId={uid}&date={udate}") def together(@Param("uid") owner: String, @Param("udate") udate: String): String

    @RequestLine("GET /calculator/users/together?userId={uid}") def justUidtogether(@Param("uid") owner: String): String

    @RequestLine("GET /") def index(): String
  }

  getConfigInstance.setProperty(client + ".ribbon.listOfServers", "172.16.40.68:8090,172.16.40.69:8090")
  getConfigInstance.setProperty(client + ".ribbon.ReadTimeout", 5000)
  getConfigInstance.setProperty(client + ".ribbon.MaxAutoRetries", 1)
  getConfigInstance.setProperty(client + ".ribbon.MaxAutoRetriesNextServer", 1)
  getConfigInstance.setProperty(client + ".ribbon.OkToRetryOnAllOperations", true)
  getConfigInstance.setProperty(client + ".ribbon.ConnectTimeout", 5000)

  class SErrorDecoder extends ErrorDecoder {

    implicit val formats: Formats = new DefaultFormats {
      override def dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }

    /**
     * @param s the method name
     * @param response
     * @return
     */
    override def decode(s: String, response: Response): Exception = {
      try {
        val ignored = Util.toString(response.body().asReader())
        val jjv: JValue = parse(ignored)
        val msg = (jjv(0) \ "logref").extract[String]
        new Exception(msg)
      } catch {
        case e: Exception => FeignException.errorStatus(s, response)
      }

      /**
       * ignored format is:
       * [{"logref": "HttpReadException","message": "can't read data with userd: 6102553"}]
       */
    }
  }

  private val risk: Risk = Feign.builder()
    .errorDecoder(new SErrorDecoder)
    .target(LoadBalancingTarget.create(classOf[Risk], "http://" + client))

  def run(uid: String, udate: String): String = {
    if (udate.length() == 0) {
      risk.justUidtogether(uid)
    }
    else {
      risk.together(uid, udate)
    }
  }

}