import sbt.Keys._
import sbt._


object FunTestSettings {
  lazy val FunTestConfig = config("fun").extend(Test)

  lazy val Value =
    inConfig(FunTestConfig)(Defaults.testSettings) ++ Seq(
      scalaSource in FunTestConfig := baseDirectory.value / "src/functional/scala",
      resourceDirectory in FunTestConfig := baseDirectory.value / "src/functional/resources",
      parallelExecution in FunTestConfig := false,
      testOptions in FunTestConfig += Tests.Argument("-oDU") // adds duration of each test
    )
}
