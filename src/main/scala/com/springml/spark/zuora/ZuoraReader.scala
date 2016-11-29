package com.springml.spark.zuora

import com.springml.spark.zuora.model.ZuoraInput
import com.springml.spark.zuora.util.XercesWarningFilter

import org.apache.log4j.Logger

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

    val nsClient = new NetSuiteClient(netSuiteInput)
    val response = nsClient.search()
    records ++= readRecords(response)

    val searchId = getSearchId(response)
    while (moreToRead(response)) {
      currentPage += 1
      logger.info("Reading page " + currentPage)
      val searchMoreResponse = nsClient.searchMoreWithId(searchId, currentPage)
      records ++= readRecords(searchMoreResponse)
    }

    records
  }
}
