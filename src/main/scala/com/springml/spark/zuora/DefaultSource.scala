package com.springml.spark.zuora

import com.springml.spark.zuora.model.ZuoraInput
import org.apache.log4j.Logger
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
import org.apache.spark.sql.sources.{BaseRelation, CreatableRelationProvider, RelationProvider, SchemaRelationProvider}
import org.apache.spark.sql.types.StructType

import scala.collection.mutable

/**
  * Created by sam on 28/11/16.
  */
class DefaultSource extends RelationProvider with SchemaRelationProvider with CreatableRelationProvider {
  @transient val logger = Logger.getLogger(classOf[DefaultSource])

  override def createRelation(sqlContext: SQLContext,
                              parameters: Map[String, String]): BaseRelation = {
    createRelation(sqlContext, parameters, null)
  }

  override def createRelation(sqlContext: SQLContext,
                              parameters: Map[String, String],
                              schema: StructType): BaseRelation = {
    val email = param(parameters, "email")
    val password = param(parameters, "password")
    val zoql = param(parameters, "zoql")
    val instanceUrl = parameters.getOrElse("instanceURL", "https://rest.zuora.com")
    val apiVersion = parameters.getOrElse("apiVersion", "38.0")

    // TODO
//    val pageSizeParam = parameters.getOrElse("pageSize", "100")
//    val pageSize = pageSizeParam.toInt
//
//    if (pageSize > 1000) {
//      sys.error("Invalid pageSize option. Maximum supported pageSize is 1000")
//    }

    val zuoraInput = new ZuoraInput(email, password, zoql, instanceUrl, apiVersion, 100)

    val records = new ZuoraReader(zuoraInput) read()
    new DatasetRelation(records, sqlContext, schema)
  }

  override def createRelation(sqlContext: SQLContext,
                              mode: SaveMode,
                              parameters: Map[String, String],
                              data: DataFrame): BaseRelation = {
    logger.error("Save not supported by Zuora connector")
    throw new UnsupportedOperationException
  }

  private def param(parameters: Map[String, String],
                    paramName: String) : String = {
    val paramValue = parameters.getOrElse(paramName,
      sys.error(s"""'$paramName' must be specified for Spark Zuora package"""))

    if ("password".equals(paramName)) {
      logger.debug("Param " + paramName + " value " + paramValue)
    }

    paramValue
  }

}
