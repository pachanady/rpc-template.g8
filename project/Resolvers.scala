import sbt._


object Resolvers {
  val values = Seq(
    "The New Motion Public Repo" at "https://nexus.thenewmotion.com/content/groups/public/",
    Resolver.bintrayRepo("bigcommerce", "mvn-private"),
    Resolver.jcenterRepo,
    Resolver.bintrayRepo("lonelyplanet", "maven")
  )
}
