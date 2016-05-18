organization := "com.bob"

name := "scalatour"

version := "1.0"

scalaVersion := "2.11.6"

scalacOptions ++= Seq(
  "-language:postfixOps",
  "-language:implicitConversions",
  "-language:reflectiveCalls",
  "-language:higherKinds",
  "-feature",
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Xfatal-warnings"
)

javaOptions += "-server -Xss1m -Xmx2g"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

libraryDependencies += "org.scala-lang.modules" % "scala-async_2.11" % "0.9.6-RC2"

libraryDependencies += "com.typesafe" % "config" % "1.3.0"

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-http" % "6.34.0",
  "com.twitter" %% "finagle-mysql" % "6.34.0",
  "com.twitter" %% "finagle-redis" % "6.34.0"
).map(_.exclude("com.google.code.findbugs", "jsr305"))

libraryDependencies += "org.json4s" %% "json4s-native" % "3.3.0"

libraryDependencies += "com.squareup.okhttp3" % "okhttp" % "3.2.0"

libraryDependencies ++= Seq(
  "com.google.inject" % "guice" % "4.0",
  "com.google.inject.extensions" % "guice-persist" % "4.0"
)

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.38"

libraryDependencies += "org.scalikejdbc" %% "scalikejdbc" % "2.3.5"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.2"

libraryDependencies += ("com.netflix.eureka" % "eureka-client" % "1.1.147")
  .exclude("javax.ws.rs", "jsr311-api")
  .exclude("commons-logging", "commons-logging")
  .exclude("xmlpull", "xmlpull")

/* 依赖okhttpclient版本为2，去掉 */
libraryDependencies ++= Seq(
  //  "com.netflix.feign" % "feign-okhttp" % "8.16.2",
  "com.netflix.feign" % "feign-ribbon" % "8.16.2"
).map(_.exclude("com.squareup.okhttp", "okhttp"))