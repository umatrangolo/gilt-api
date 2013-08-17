import de.johoop.jacoco4sbt._
import JacocoPlugin._

name := "gilt-api"

organization := "com.umatrangolo"

version := "0.0.1"

scalaVersion in ThisBuild := "2.10.2"

libraryDependencies ++= Seq(
                    "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2",
                    "com.fasterxml.jackson.core" % "jackson-annotations" % "2.2.2",
                    "com.fasterxml.jackson.core" % "jackson-core" % "2.2.2",
                    "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % "2.2.2",
                    "com.google.guava" % "guava" % "14.0.1",
                    "joda-time" % "joda-time" % "2.2",
                    "org.joda" % "joda-convert" % "1.2",
                    "com.ning" % "async-http-client" % "1.7.17",
                    "org.slf4j" % "slf4j-api" % "1.7.5",
                    "ch.qos.logback" % "logback-classic" % "1.0.13",
                    "com.google.code.findbugs" % "jsr305" % "1.3.9"
                    )

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"

seq(jacoco.settings : _*)

resolvers ++= Seq(
  "Sonatype.org Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype.org Releases" at "http://oss.sonatype.org/service/local/staging/deploy/maven2"
)

publishMavenStyle := true

//publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "/service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

homepage := Some(url("https://github.com/umatrangolo/gilt-api"))

pomIncludeRepository := { _ => false }

pomExtra := (
  <scm>
    <url>git@github.com:umatrangolo/gilt-api.git</url>
    <connection>scm:git:git@github.com:umatrangolo/gilt-api.git</connection>
  </scm>
  <developers>
    <developer>
      <id>umatrangolo</id>
      <name>Ugo Matrangolo</name>
      <url>http://umatrangolo.com</url>
    </developer>
  </developers>
)
