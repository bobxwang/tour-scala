logLevel := Level.Warn

addDependencyTreePlugin

resolvers += Resolver.url("https://github.com/sbt/sbt-assembly.git")

resolvers += Resolver.url("https://github.com/google/guice.git")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "4.3.0")

// 可以像mvn一样显示依赖关系图
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.2")

resolvers += Resolver.url("https://repo.typesafe.com/typesafe/ivy-releases/")

resolvers += Resolver.url("https://repo.typesafe.com/typesafe/maven-releases/")

resolvers ++= Seq(
  "Spray repository" at "https://repo.spray.io"
)