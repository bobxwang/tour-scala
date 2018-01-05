baseAssemblySettings

mainClass in assembly := Some("com.bob.scalatour.IOTips")

assemblyJarName in assembly := "com.bob.nothing.jar"

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs@_*) => MergeStrategy.first
  case PathList(ps@_*) if ps.last endsWith ".html" => MergeStrategy.first
  case "application.conf" => MergeStrategy.concat
  case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.concat
  case x if x.endsWith("BUILD") => MergeStrategy.last
  case "unwanted.txt" => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    if(oldStrategy == MergeStrategy.deduplicate)
      MergeStrategy.first
    else
      oldStrategy(x)
}