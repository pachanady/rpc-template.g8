import bintray.BintrayKeys._
import sbt.Keys._


object CommonSettings {
  val settings = Seq(
    organization := "com.bigcommerce",
    scalaVersion := "2.13.1",
    bintrayOrganization := Some("bigcommerce"),
    bintrayOmitLicense  := true,
    bintrayRepository   := "mvn-private",
    resolvers ++= Resolvers.values
  )

  val scalacOpts = Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-encoding", "utf8"
  )
}
