name := "gilt-api-scala-example"

scalaVersion in ThisBuild := "2.10.2"

libraryDependencies ++= Seq(
                    "org.slf4j" % "slf4j-api" % "1.7.5",
                    "ch.qos.logback" % "logback-classic" % "1.0.13"
                    )

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"
