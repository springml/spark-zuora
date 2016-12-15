package com.springml.spark.zuora

import org.scalatest.{BeforeAndAfterEach, FunSuite}

import scala.xml.XML

/**
  * Created by sam on 9/12/16.
  */
class ZuoraReaderTest extends FunSuite with BeforeAndAfterEach {

  test("Test QueryLocator") {
    val reader = new ZuoraReader(zuoraInput = null)
    val response = getResourceContent("/SampleZuoraQueryResponse.xml")

    val responseXML = XML.loadString(response)
    val queryLocator = reader.queryLocator(responseXML)

    assert("2c92c0f8586bca0801586c5adba35c73-5".equals(queryLocator))
  }

  test("Test moreToRead") {
    val reader = new ZuoraReader(zuoraInput = null)
    val response = getResourceContent("/SampleZuoraQueryResponse.xml")

    val responseXML = XML.loadString(response)
    val moreToRead = reader.moreToRead(responseXML)

    assert(moreToRead)
  }

  test("Test readRecords") {
    val reader = new ZuoraReader(zuoraInput = null)
    val response = getResourceContent("/SampleZuoraQueryResponse.xml")

    val responseXML = XML.loadString(response)
    val records = reader.readRecords(responseXML)

    assert(records.size == 5)
  }

  private def getResourceContent(path: String): String = {
    val resStream = getClass().getResourceAsStream(path)
    scala.io.Source.fromInputStream(resStream).mkString
  }

}
