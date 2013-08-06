package com.umatrangolo.giltapi.utils

object ApiKey {
  // TODO fetch the key from (i) System.getProperty or (ii) giltapi.properties in the classpath or None
  //      if both failed
  lazy val value: Option[String] = Some("b744156e9efa7ef31abf78d030d99ba3")
}
