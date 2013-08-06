package com.umatrangolo.giltapi.utils

import scala.util.control.Exception._
import java.util.Properties
import java.io.IOException

/**
 * Tries to fetch the ApiKey first from the sys properties and then
 * from the `giltapi.properties` file in the classpath.
 */
object ApiKey {
  val ApiKeyProperty = "giltapi.client.apikey"

  lazy val value: Option[String] = Option(System.getProperty(ApiKeyProperty)).orElse {
    val props = new Properties()
    val is = catching(classOf[IOException]) opt this.getClass.getClassLoader.getResourceAsStream("giltapi.properties")
    is.map { s =>
      props.load(s)
      props.getProperty(ApiKeyProperty)
    }
  }
}
