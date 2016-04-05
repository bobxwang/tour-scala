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
  "com.twitter" %% "finagle-mysql" % "6.34.0"
).map(_.exclude("com.google.code.findbugs", "jsr305"))