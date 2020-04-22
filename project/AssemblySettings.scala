import sbt.Keys.{aggregate, mainClass, test}
import sbtassembly.AssemblyKeys._
import sbtassembly.AssemblyPlugin.autoImport.{MergeStrategy, assemblyJarName, assemblyMergeStrategy}
import sbtassembly.PathList


object AssemblySettings {

  val MergingStrategy =
    assemblyMergeStrategy in assembly := {
      case PathList(xs @ _*) if xs.last == "io.netty.versions.properties" => MergeStrategy.rename
      case PathList("com", "bigcommerce", "common", "errors", _*)         => MergeStrategy.last
      case PathList("com", "google", "protobuf", _*)                      => MergeStrategy.first
      case PathList("com", "google", "api", _*)                           => MergeStrategy.first
      case PathList("logback.xml")                                        => MergeStrategy.last
      case PathList("logback-nomad.xml")                                  => MergeStrategy.last
      case PathList("com", "bigcommerce", "core", _*)                     => MergeStrategy.first
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        if (oldStrategy == MergeStrategy.deduplicate)
          MergeStrategy.first
        else
          oldStrategy(x)
    }

  object Root {
    val values = Seq(
      test in assembly := {},
      aggregate in assembly := false,
      assemblyJarName := "catalog.jar",
      mainClass in assembly := Some("com.bigcommerce.catalog.Boot"),
      MergingStrategy
    )
  }
}
