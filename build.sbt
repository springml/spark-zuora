
name := "spark-zuora"
version := "1.0.0"
organization := "com.springml"

scalaVersion := "2.10.6"

resolvers += "bintray-spark-packages" at "https://dl.bintray.com/spark-packages/maven/"
resolvers += Resolver.url("artifactory", url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
resolvers += "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
resolvers += "Spark Package Main Repo" at "https://dl.bintray.com/spark-packages/maven"

libraryDependencies ++= Seq(
  "org.mockito" % "mockito-core" % "2.1.0-RC.1",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

libraryDependencies += "org.springframework.ws" % "spring-ws-core" % "2.3.0.RELEASE"

parallelExecution in Test := false

// Spark Package Details (sbt-spark-package)
spName := "springml/spark-zuora"
spAppendScalaVersion := true
sparkVersion := "1.6.2"
sparkComponents += "sql"

assemblyMergeStrategy in assembly := {
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}

// Maven Details
publishMavenStyle := true
spIncludeMaven := true
spShortDescription := "Spark NetSuite Connector"
spDescription := """Spark NetSuite Connector
                   | - Creates dataframe using data fetched from NetSuite Web services """.stripMargin

// licenses += "Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0")
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <url>https://github.com/springml/spark-zuora</url>
    <licenses>
      <license>
        <name>Apache License, Verision 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com/springml/spark-zuora</connection>
      <developerConnection>scm:git:git@github.com:springml/spark-zuora</developerConnection>
      <url>github.com/springml/spark-zuora</url>
    </scm>
    <developers>
      <developer>
        <id>springml</id>
        <name>Springml</name>
        <url>http://www.springml.com</url>
      </developer>
    </developers>)
