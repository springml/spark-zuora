package com.springml.spark.zuora.ws

import com.springml.spark.zuora.model.ZuoraInput
import com.springml.spark.zuora.ws.SoapHeaderHandler
import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
  * Created by sam on 12/12/16.
  */
class SoapHeaderHandlerTest extends FunSuite with BeforeAndAfterEach {
  val instanceUrl = "https://apisandbox.zuora.com"
  val apiVersion = "38.0"
  val zoql = "select AccountId, FirstName, LastName from contact"
  val email = "zuora-user@zuora.com"
  val password = "zuora"
  val pageSize = 100
  val sessionId = "TEST_SESSION_ID"
  val soapAction = "Query"

  val zuoraInput = new ZuoraInput(email, password, zoql, instanceUrl, apiVersion, pageSize)
  val soapHeaderHandler = new SoapHeaderHandler(zuoraInput, sessionId, soapAction)

  test("Test SessionHeader") {
    val sessionHeader = soapHeaderHandler.sessionHeader

    assert(sessionHeader.contains(sessionId))
  }

  test("Test QueryOptions") {
    val queryOptionsHeader = soapHeaderHandler.sessionHeader

    assert(queryOptionsHeader.contains(pageSize))
  }
}
