import sbt._
import Keys._

object GiltApiBuild extends Build {

  lazy val root = Project(
    id = "gilt-api",
    base = file(".")
  ).dependsOn(core, api, client).aggregate(api, core, client)

  lazy val api = Project(
    id = "api",
    base = file("api")
  ).dependsOn(core)

  lazy val core = Project(
    id = "core",
    base = file("core")
  )

  lazy val client = Project(
    id = "client",
    base = file("client")
  ).dependsOn(api, core)
}
