scalikejdbcSettings

organization := "com.bob"

name := "scalatour"

version := "1.0"

scalaVersion := "2.11.8"

initialize := {
  assert(
    Integer.parseInt(sys.props("java.specification.version").split("\\.")(1))
      >= 7,
    "Java 7 or above required")
}

//-Yno-adapted-args --> 避免scala编译器在方法参数上自作聪明的适配
//-Xlint --> 已经包含Ywarn-adapted-args,只是警告还是能编译过去
scalacOptions ++= Seq(
  "-language:postfixOps"
  , "-language:implicitConversions"
  , "-language:reflectiveCalls"
  , "-language:higherKinds"
  , "-feature"
  , "-unchecked"
  , "-deprecation"
  , "-Xlint"
  , "-Xfatal-warnings"
  , "-encoding", "utf8"
  , "-Yno-adapted-args"
  //  , "-Ywarn-dead-code"
  //  , "-Ywarn-unused"
  //  , "-Ywarn-unused-import"
)

javaOptions += "-server -Xss1m -Xmx2g"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.8.3" % "test")

libraryDependencies += "org.scala-lang.modules" % "scala-async_2.11" % "0.9.6-RC2"

libraryDependencies += "com.typesafe" % "config" % "1.3.0"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.38"

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-http" % "6.44.0",
  "com.twitter" %% "finagle-mysql" % "6.44.0",
  "com.twitter" %% "finagle-redis" % "6.44.0"
).map(_.exclude("com.google.code.findbugs", "jsr305"))

libraryDependencies += "org.json4s" %% "json4s-native" % "3.3.0"

libraryDependencies += "com.squareup.okhttp3" % "okhttp" % "3.9.1"

libraryDependencies ++= Seq(
  "com.google.inject" % "guice" % "4.0",
  "com.google.inject.extensions" % "guice-persist" % "4.0"
)

libraryDependencies ++= Seq(
  "net.sourceforge.htmlunit" % "htmlunit-core-js" % "2.23",
  "net.sourceforge.htmlunit" % "htmlunit" % "2.23"
)

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "2.4.2",
  "org.scalikejdbc" %% "scalikejdbc-test" % "2.4.2" % "test",
  "org.scalikejdbc" %% "scalikejdbc-mapper-generator-core" % "2.4.2"
)

libraryDependencies += ("org.apache.kafka" % "kafka_2.10" % "0.8.2.0")
  .exclude("org.apache.zookeeper", "zookeeper")

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.2",
  "org.scalaz" %% "scalaz-effect" % "7.2.2"
)

libraryDependencies += ("com.netflix.eureka" % "eureka-client" % "1.1.147")
  .exclude("javax.ws.rs", "jsr311-api")
  .exclude("xmlpull", "xmlpull")
  .excludeAll(ExclusionRule(organization = "commons-logging"))

/* 依赖okhttpclient版本为2，去掉 */
libraryDependencies ++= Seq(
  //  "com.netflix.feign" % "feign-okhttp" % "8.16.2",
  "com.netflix.feign" % "feign-ribbon" % "8.16.2"
).map(_.exclude("com.squareup.okhttp", "okhttp"))

//libraryDependencies += "org.msgpack" %% "msgpack-scala" % "0.6.11"

// a consistency, persistance and performance map using off-heap
//libraryDependencies += "net.openhft" % "chronicle-map" % "3.8.0"

// a better file operation
libraryDependencies += "com.github.pathikrit" %% "better-files" % "2.16.0"

// 字符串相似性试题算法库
libraryDependencies += "com.rockymadden.stringmetric" %% "stringmetric-core" % "0.27.4"

val akkaverstion = "2.4.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.11" % akkaverstion,
  "com.typesafe.akka" % "akka-slf4j_2.11" % akkaverstion,
  "com.typesafe.akka" % "akka-remote_2.11" % akkaverstion,
  "com.typesafe.akka" % "akka-agent_2.11" % akkaverstion,
  "com.typesafe.akka" % "akka-cluster_2.11" % akkaverstion,
  "com.typesafe.akka" % "akka-cluster-metrics_2.11" % akkaverstion
)

libraryDependencies += "org.jsoup" % "jsoup" % "1.10.3"

// 当一个包不知道是被什么包给引入的时候, 又想排除此包, 可以使用此
// stax/stax-api/1.0.1 跟xml-apis/xml-apis/1.4.01包一样, 而且后者更强大
libraryDependencies := libraryDependencies.value.map(_.exclude("stax", "stax-api"))
// 或者使用排除包总的, 使用这两个都可以排除包
excludeDependencies += "com.example" %% "foo"