import de.johoop.jacoco4sbt._
import JacocoPlugin._

name := "gilt-api"

organization := "com.umatrangolo"

version := "0.0.1-SNAPSHOT"

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
                    "ch.qos.logback" % "logback-classic" % "1.0.13"
                    )

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"

seq(jacoco.settings : _*)