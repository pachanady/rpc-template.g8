import sbt._

object Dependencies {

  object BigCommerce {

    object Interfaces {
      val Version = "1.12.0-p27272"
      val Dependency: Seq[ModuleID] = Seq("com.bigcommerce" %% "services-scala" % Version)
    }

    object ScalaCore {
      val Version = "5.0.2"

      val Dependency: Seq[ModuleID] = Seq(
        "com.bigcommerce" %% "scala-core" % Version excludeAll(
          ExclusionRule(organization = "commons-logging"),
          ExclusionRule(organization = "com.bigcommerce", name = "services-scala_2.13")
        )
      )
    }

  }

  object Cats {
    val Version = "2.1.1"
    val Core: Seq[ModuleID] = Seq("org.typelevel" %% "cats-core" % Version)
  }

  object Guice {

    object ScalaGuice {
      val Version = "4.2.6"
      val Dependency: Seq[ModuleID] = Seq("net.codingwell" %% "scala-guice" % Version)
    }

    object Testing {
      val Version = "4.2.2"
      val Dependency: Seq[ModuleID] = Seq("com.google.inject.extensions" % "guice-testlib" % Version)
    }

  }

  object Logging {

    object Sentry {
      val Version = "1.7.16"
      val Dependency: Seq[ModuleID] = Seq("io.sentry" % "sentry-logback" % Version)
    }

    object Logback {
      val Dependencies: Seq[ModuleID] = Seq(
        "net.logstash.logback" % "logstash-logback-encoder" % "5.1",
        // for logback EvaluatorFilter
        "org.codehaus.janino" % "janino" % "3.0.12"
      )
    }

  }

  object Prometheus {
    val Http: Seq[ModuleID] = Seq("com.lonelyplanet" %% "prometheus-akka-http" % "0.5.0")
  }

  object Testing {
    val Dependencies: Seq[ModuleID] =
      Seq(
        "org.scalatest" %% "scalatest" % "3.1.1" % Test,
        "org.scalamock" %% "scalamock" % "4.4.0" % Test
      )
  }

  implicit class RichScope(modules: Seq[ModuleID]) {
    def withScopes(scopes: String): Seq[ModuleID] = modules.map(_ % scopes)
  }

}
