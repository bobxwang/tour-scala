package com.bob.scalatour

import com.gargoylesoftware.htmlunit._
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.util.FalsifyingWebConnection

/**
 * htmlunit
 */
object Crewler {

  class SimpleFalsifyingWebConnection(webClient: WebClient) extends FalsifyingWebConnection(webClient) {
    override def getResponse(request: WebRequest): WebResponse = {
      val rs = super.getResponse(request)
      if (request.getUrl.toString == "http://www.tianyancha.com:80/company/54859844.json") {
        val targetJson = rs.getContentAsString
        println(targetJson)
      }
      rs
    }
  }

  def main(args: Array[String]) {

    val webClient = new WebClient(BrowserVersion.CHROME)
    new SimpleFalsifyingWebConnection(webClient)
    webClient.getCookieManager.setCookiesEnabled(true)
    webClient.getOptions.setCssEnabled(false)
    webClient.getOptions().setRedirectEnabled(true)
    webClient.getOptions.setJavaScriptEnabled(true)
    webClient.getOptions.setThrowExceptionOnFailingStatusCode(false)
    webClient.setAjaxController(new NicelyResynchronizingAjaxController())
    webClient.getOptions.setThrowExceptionOnScriptError(false)
    val page: HtmlPage = webClient.getPage("http://www.tianyancha.com/company/54859844")
    println(page)
  }

}
