logLevel := Level.Warn

resolvers += Resolver.url("https://github.com/sbt/sbt-assembly.git")

resolvers += Resolver.url("https://github.com/jrudolph/sbt-dependency-graph.git")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += Resolver.url("https://github.com/google/guice.git")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.1")

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.38"

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "2.4.2")

// 可以像mvn一样显示依赖关系图
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")

resolvers += Resolver.url("http://dl.bintray.com/typesafe/ivy-releases/")

resolvers += Resolver.url("http://dl.bintray.com/typesafe/maven-releases/")

resolvers ++= Seq(
  "Spray repository" at "http://repo.spray.io",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)