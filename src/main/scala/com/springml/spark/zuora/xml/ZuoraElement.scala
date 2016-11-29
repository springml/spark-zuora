package com.springml.spark.zuora.xml

/**
  * Created by sam on 29/11/16.
  */
case class ZuoraElement(
                    prefix : String,
                    localName : String,
                    namespace : String,
                    value : String,
                    childs : List[ZuoraElement]
                  ) {

  override def toString: String = {
    val sb = new StringBuilder()
    sb.append("<")
    if (prefix != null) {
      sb.append(prefix)
      sb.append(":")
    }

    sb.append(localName)
    sb.append(" ")
    if (namespace != null) {
      sb.append("xmlns:").append(prefix).append("=")
      sb.append("\"").append(namespace).append("\"")
    }

    sb.append(">")
    if (childs != null && childs.size > 0) {
      val childXml = childs.mkString("")
      if (childXml != null) {
        sb.append(childXml)
      }
    }

    if (value != null) {
      sb.append(value)
    }

    sb.append("</")
    if (prefix != null) {
      sb.append(prefix)
      sb.append(":")
    }

    sb.append(localName)
    sb.append(">")

    sb.toString()
  }

}
