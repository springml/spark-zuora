# Spark Zuora Library

A Library to fetch data from Zuora and construct Spark dataframe. This library executes [ZOQL|https://knowledgecenter.zuora.com/DC_Developers/SOAP_API/M_Zuora_Object_Query_Language] using [Zuora REST API|https://knowledgecenter.zuora.com/DC_Developers/REST_API] to fetch data from Zuora

## Requirements

This library requires Spark 1.6+

## Linking
You can link against this library in your program at the following ways:

### Maven Dependency
```
<dependency>
    <groupId>com.springml</groupId>
    <artifactId>spark-zuora_2.10</artifactId>
    <version>1.0.0</version>
</dependency>
```

### SBT Dependency
```
libraryDependencies += "com.springml" % "spark-zuora_2.10" % "1.0.0"
```

## Using with Spark shell
This package can be added to Spark using the `--packages` command line option.  For example, to include it when starting the spark shell:

```
$ bin/spark-shell --packages com.springml:spark-zuora_2.10:1.0.0
```

## Feature
* **Construct Spark Dataframe using Zuora data** - User has to provide ZOQL (Zuora Object Query Language). ZOQL will be executed using Zuora REST API. It uses [Query action|https://www.zuora.com/developer/api-reference/] to execute ZOQL. And further records are accessed using [QueryMore Action|https://www.zuora.com/developer/api-reference/]. 

### Options
* `email`: Zuora account user Id
* `password`: Zuora account password
* `instanceURL`: Zuora Instance URL. Like, https://api.zuora.com. Possible values are listed [here|https://knowledgecenter.zuora.com/DC_Developers/REST_API/A_REST_basics#URLS_and_Endpoints]
* `pageSize`: (Optional) Number of records to pulled in a single request. Max pageSze is 2000. Default value is `1000`.
* `zoql`: ZOQL to be executed to fetch records from Zuora. Example, ```select AccountId, FirstName, LastName from contact```. More details on ZOQL can be found [here|https://knowledgecenter.zuora.com/DC_Developers/SOAP_API/M_Zuora_Object_Query_Language]
* `schema`: (Optional) Schema to be used for constructing dataframes. If not provided all fields will be of type String

### Scala API
Spark 1.6+:
```scala
import org.apache.spark.sql.SQLContext

// Construct Dataframe from Zuora records
val sqlContext = new SQLContext(sc)
// Here "select AccountId, FirstName, LastName from contact" is executed
val zoql = "select AccountId, FirstName, LastName from contact"

// Below constructs dataframe by executing Query and QueryMore Action 
val df = sqlContext.read.
    format("com.springml.spark.zuora").
    option("email", "zuora_email").
    option("password", "zuora_password").
    option("instanceURL", "https://apisandbox.zuora.com").
    option("zoql", zoql).
    option("pageSize", "2000").
    load() 

```


### R API
Spark 1.6+:
```r
# Construct Dataframe from Zuora records
# Here "select AccountId, FirstName, LastName from contact" is executed
zoql <- "select AccountId, FirstName, LastName from contact"

// Below constructs dataframe by executing Query and QueryMore Action
df <- read.df(sqlContext, 
      source="com.springml.spark.zuora", 
      email="zuora_email", 
      password="zuora_password", 
      instanceURL="https://apisandbox.zuora.com",
      pageSize="100",
      zoql=zoql)

```


## Building From Source
This library is built with [SBT](http://www.scala-sbt.org/0.13/docs/Command-Line-Reference.html), which is automatically downloaded by the included shell script. To build a JAR file simply run `sbt/sbt package` from the project root. The build configuration includes support for both Scala 2.10 and 2.11.
