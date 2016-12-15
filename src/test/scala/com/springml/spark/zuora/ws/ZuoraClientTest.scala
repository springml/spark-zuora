package com.springml.spark.zuora.ws

import com.springml.spark.zuora.model.ZuoraInput
import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
  * Created by sam on 12/12/16.
  */
class ZuoraClientTest extends FunSuite with BeforeAndAfterEach {
  val instanceUrl = "https://apisandbox.zuora.com"
  val apiVersion = "38.0"

  test("Test SessionId") {
    val client = new ZuoraClient(zuoraInput = null)
    val response = getResourceContent("/SampleZuoraLoginResponse.xml")

    val sessionId = client.sessionId(response)

    assert("TEST_SESSION_ID".equals(sessionId))
  }

  test("Test Query More Request") {
    val QUERY_LOCATOR = "2c92c0f8586bca0801586c5adba35c73-5"
    val client = new ZuoraClient(zuoraInput = null)
    val queryMoreRequest = client.queryMoreRequest(QUERY_LOCATOR)

    assert(queryMoreRequest.contains(QUERY_LOCATOR))
  }

  test("Test Query Request") {
    val zoql = "select AccountId, FirstName, LastName from contact"

    val zuoraInput = new ZuoraInput(email = null, password = null,
      zoql = zoql, instanceUrl = instanceUrl, apiVersion = apiVersion, pageSize = 100)

    val client = new ZuoraClient(zuoraInput)
    val queryRequest = client.queryRequest

    assert(queryRequest.contains(zoql))
  }

  test("Test Login Request") {
    val email = "zuora-user@zuora.com"
    val password = "zuora"

    val zuoraInput = new ZuoraInput(email = email, password = password,
      zoql = null, instanceUrl = instanceUrl, apiVersion = apiVersion, pageSize = 100)

    val client = new ZuoraClient(zuoraInput)
    val loginRequest = client.loginRequest

    assert(loginRequest.contains(email))
    assert(loginRequest.contains(password))
  }

  private def getResourceContent(path: String): String = {
    val resStream = getClass().getResourceAsStream(path)
    scala.io.Source.fromInputStream(resStream).mkString
  }

}
