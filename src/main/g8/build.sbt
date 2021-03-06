import Dependencies._

lazy val common = Project(id = "common", base = file("common"))
  .settings(CommonSettings.settings)
  .settings(scalacOptions ++= CommonSettings.scalacOpts)
  .settings(AssemblySettings.MergingStrategy)
  .settings(
    libraryDependencies ++=
      BigCommerce.Interfaces.Dependency ++
        BigCommerce.ScalaCore.Dependency ++
        Cats.Core ++
        Guice.ScalaGuice.Dependency ++
        Guice.Testing.Dependency ++
        Logging.Logback.Dependencies ++
        Logging.Sentry.Dependency ++
        Testing.Dependencies
  )
  .settings(logBuffered in Test := false)
  .disablePlugins(RevolverPlugin)

// Main subproject
lazy val $module$ = Project(id = "$module$-impl", base = file("$module$"))
  .settings(CommonSettings.settings)
  .settings(scalacOptions ++= CommonSettings.scalacOpts)
  .settings(AssemblySettings.MergingStrategy)
  .configs(FunTestSettings.FunTestConfig)
  .settings(FunTestSettings.Value)
  .settings(
    libraryDependencies ++=
      Guice.Testing.Dependency.withScopes("test,fun") ++
        Testing.Dependencies
  )
  .dependsOn(common)

lazy val root = Project(id = "$name$", base = file("."))
  .settings(CommonSettings.settings)
  .settings(scalacOptions ++= CommonSettings.scalacOpts)
  .settings(Revolver.settings)
  .settings(
    libraryDependencies ++=
      Prometheus.Http ++
        Testing.Dependencies
  )
  .settings(
    logBuffered in Test := false,
    testOptions in Test += Tests.Argument("-oDU") // adds duration of each test
  )
  .settings(AssemblySettings.Root.values)
  .dependsOn($module$)

lazy val testAll = TaskKey[Unit]("testAll", "Execute all tests")
testAll in Runtime := Def
  .sequential(
    test in (root, Test),
    test in ($module$, Test),
    test in (common, Test),
    test in ($module$, FunTestSettings.FunTestConfig)
  )
  .value
