// give the user a nice default project!
ThisBuild / organization := "com.bigcommerce.rpc"
ThisBuild / scalaVersion := "2.12.8"

lazy val root = (project in file(".")).
  settings(
    name := "rpc-template"
  )
