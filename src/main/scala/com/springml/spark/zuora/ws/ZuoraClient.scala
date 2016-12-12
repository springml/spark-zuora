package com.springml.spark.zuora.ws

import java.io.{StringReader, StringWriter}
import javax.xml.transform.stream.{StreamResult, StreamSource}

import com.springml.spark.zuora.Constants
import com.springml.spark.zuora.model.ZuoraInput
import com.springml.spark.zuora.xml.ZuoraElement
import org.apache.log4j.Logger
import org.springframework.ws.client.core.WebServiceTemplate

import scala.xml.XML

/**
  * Created by sam on 29/11/16.
  */
class ZuoraClient(
                 zuoraInput: ZuoraInput
                 ) {
  @transient val logger = Logger.getLogger(classOf[ZuoraClient])

  def login : String = {
    val response = execute(null, loginRequest)
    sessionId(response)
  }

  def query(sessionId : String): String = {
    val soapHeaderHandler = new SoapHeaderHandler(zuoraInput, sessionId, "query")
    val response = execute(soapHeaderHandler, queryRequest)

    response
  }

  def queryMore(sessionId : String, queryLocator: String): String = {
    val soapHeaderHandler = new SoapHeaderHandler(zuoraInput, sessionId, "queryMore")
    val response = execute(soapHeaderHandler, queryMoreRequest(queryLocator))

    response
  }

  def sessionId(response : String): String = {
    val responseXml = XML.loadString(response)
    val seq = (responseXml \ "result" \ "Session")
    (responseXml \ "result" \ "Session") text
  }

  def queryMoreRequest(queryLocator: String): String = {
    val queryLocatorElem = ZuoraElement(Constants.PREFIX, Constants.ELEM_QUERY_LOCATOR,
      Constants.NAMESPACE, queryLocator, null)
    val queryMore = ZuoraElement(Constants.PREFIX, Constants.ELEM_QUERY_MORE,
      Constants.NAMESPACE, null, List(queryLocatorElem))

    queryMore.toString
  }

  def queryRequest: String = {
    val queryString = ZuoraElement(Constants.PREFIX, Constants.ELEM_QUERY_STRING,
      Constants.NAMESPACE, zuoraInput.zoql, null)
    val query = ZuoraElement(Constants.PREFIX, Constants.ELEM_QUERY,
      Constants.NAMESPACE, null, List(queryString))

    query.toString
  }

  def loginRequest: String = {
    val username = ZuoraElement(Constants.PREFIX, Constants.ELEM_USERNAME,
      Constants.NAMESPACE, zuoraInput.email, null)
    val password = ZuoraElement(Constants.PREFIX, Constants.ELEM_PASSWORD,
      Constants.NAMESPACE, zuoraInput.password, null)

    val login = ZuoraElement(Constants.PREFIX, Constants.ELEM_LOGIN,
      Constants.NAMESPACE, null, List(username, password))

    login.toString
  }

  def webServiceTemplate : WebServiceTemplate = {
    val webServiceTemplate = new WebServiceTemplate
    logger.info("Zuora Endpoint : " + defaultUri)
    webServiceTemplate.setDefaultUri(defaultUri)

    webServiceTemplate
  }

  private def defaultUri: String = {
    zuoraInput.instanceUrl + "/apps/services/a/" + zuoraInput.apiVersion
  }

  private def execute(soapHeaderHandler: SoapHeaderHandler, request : String) : String = {
    logger.info("Request : " + request)
    val source = new StreamSource(new StringReader(request))
    val writer = new StringWriter
    val streamResult = new StreamResult(writer)
    webServiceTemplate.sendSourceAndReceiveToResult(source, soapHeaderHandler, streamResult)

    val response = writer.toString
    logger.info("Response : " + response)

    return response
  }
}
