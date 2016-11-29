package com.springml.spark.zuora.ws

import javax.xml.transform.TransformerFactory

import com.springml.spark.zuora.Constants
import com.springml.spark.zuora.model.ZuoraInput
import com.springml.spark.zuora.xml.ZuoraElement
import org.springframework.ws.WebServiceMessage
import org.springframework.ws.client.core.WebServiceMessageCallback
import org.springframework.ws.soap.SoapMessage
import org.springframework.xml.transform.StringSource
/**
  * Created by sam on 29/11/16.
  */
class SoapHeaderHandler(
                       zuoraInput: ZuoraInput,
                       sessionId: String,
                       soapAction: String
                       ) extends WebServiceMessageCallback {

  override def doWithMessage(message: WebServiceMessage): Unit = {
    val soapMessage = message.asInstanceOf[SoapMessage]
    soapMessage.setSoapAction(soapAction)

    val soapHeader = soapMessage.getSoapHeader
    val transformer = TransformerFactory.newInstance().newTransformer()
    transformer.transform(new StringSource(queryOptions), soapHeader.getResult)
    transformer.transform(new StringSource(sessionHeader), soapHeader.getResult)
  }

  private def sessionHeader: String = {
    val batchSize = ZuoraElement(Constants.PREFIX, Constants.ELEM_BATCH_SIZE,
      Constants.NAMESPACE, zuoraInput.pageSize.toString, null)
    // TODO - Do we need to get this as option?
    val caseSensitive = ZuoraElement(Constants.PREFIX, Constants.ELEM_CASE_SENSITIVE,
      Constants.NAMESPACE, Constants.STR_FALSE, null)
    val fastQuery = ZuoraElement(Constants.PREFIX, Constants.ELEM_FAST_QUERY,
      Constants.NAMESPACE, Constants.STR_FALSE, null)


    val queryOptions = ZuoraElement(Constants.PREFIX, Constants.ELEM_QUERY_OPTIONS,
      Constants.NAMESPACE, null, List(batchSize, caseSensitive, fastQuery))

    queryOptions.toString()
  }

  private def queryOptions: String = {
    val session = ZuoraElement(Constants.PREFIX, Constants.ELEM_SESSION,
      Constants.NAMESPACE, sessionId, null)

    val sessionHeader = ZuoraElement(Constants.PREFIX, Constants.ELEM_SESSION_HEADER,
      Constants.NAMESPACE, null, List(session))

    sessionHeader.toString()
  }
}
