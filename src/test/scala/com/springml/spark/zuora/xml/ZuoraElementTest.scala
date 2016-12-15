package com.springml.spark.zuora.xml

import com.springml.spark.zuora.Constants
import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
  * Created by sam on 12/12/16.
  */
class ZuoraElementTest extends FunSuite with BeforeAndAfterEach {
  val zoql = "select AccountId, FirstName, LastName from contact"

  test("Element construction") {
    val queryString = ZuoraElement(Constants.PREFIX, Constants.ELEM_QUERY_STRING,
      Constants.NAMESPACE, zoql, null)
    val query = ZuoraElement(Constants.PREFIX, Constants.ELEM_QUERY,
      Constants.NAMESPACE, null, List(queryString))

    val expectedQueryRequest = getResourceContent("/SampleZuoraQueryRequest.xml")
    println("expectedQueryRequest " + expectedQueryRequest)
    println("actual query result : " + query.toString)
    assert(expectedQueryRequest.equals(query.toString))
  }

  private def getResourceContent(path: String): String = {
    val resStream = getClass().getResourceAsStream(path)
    scala.io.Source.fromInputStream(resStream).mkString
  }

}
