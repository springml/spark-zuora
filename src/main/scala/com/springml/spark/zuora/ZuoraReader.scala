package com.springml.spark.zuora

import com.springml.spark.zuora.model.ZuoraInput
import com.springml.spark.zuora.util.XercesWarningFilter
import com.springml.spark.zuora.ws.ZuoraClient
import org.apache.log4j.Logger

import scala.collection.immutable.Seq
import scala.collection.mutable
import scala.xml.{Elem, Node, XML}

/**
  * Created by sam on 29/11/16.
  */
class ZuoraReader(
                 zuoraInput: ZuoraInput
                 ) {
  @transient val logger = Logger.getLogger(classOf[ZuoraReader])

  def read() : List[scala.collection.mutable.Map[String, String]] = {
    XercesWarningFilter.start()
    var records :List[scala.collection.mutable.Map[String, String]] = List.empty

    val zuoraClient = new ZuoraClient(zuoraInput)
    logger.info("Login into Zuora API using provided credentials")
    val sessionId = zuoraClient.login
    logger.info("Session ID : "  + sessionId)

    var currentPage = 1
    logger.info("Reading page " + currentPage)
    val response = zuoraClient.query(sessionId)
    var responseXml = XML.loadString(response)
    records ++= readRecords(responseXml)

    //    val searchId = getSearchId(response)
    while (moreToRead(responseXml)) {
      currentPage += 1
      logger.info("Reading page " + currentPage)
      val searchMoreResponse = zuoraClient.queryMore(sessionId, queryLocator(responseXml))
      responseXml = XML.loadString(searchMoreResponse)
      records ++= readRecords(responseXml)
    }

    records
  }


  private def queryLocator(responseXml: Elem): String = {
    val queryLocElem = responseXml \ "result" \ "queryLocator"

    queryLocElem.text
  }

  private def moreToRead(responseXml: Elem): Boolean = {
    val doneElem = responseXml \ "result" \ "done"

    !doneElem.text.toBoolean
  }

  private def readRecords(responseXml: Elem): Seq[mutable.Map[String, String]] = {
    val recordsXML = responseXml \ "result" \ "records"

    recordsXML.map(node => record(node))
  }

  private def record(result: Node): scala.collection.mutable.Map[String, String] = {
    val values = result.child
    val record = scala.collection.mutable.Map.empty[String, String]
    values.foreach(elem => {
      if (elem.label != null && !elem.label.isEmpty && !elem.label.contains("PCDATA")) {
        record(elem.label) = elem.text
      }
    })

    record
  }
}
