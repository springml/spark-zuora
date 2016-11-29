package com.springml.spark.zuora.model

/**
  * Created by sam on 29/11/16.
  */
class ZuoraInput(
                val email : String,
                val password : String,
                val zoql : String,
                val instanceUrl : String,
                val apiVersion : String,
                val pageSize : Integer) {

}
