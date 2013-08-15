package com.umatrangolo.giltapi

import java.net.URL
import java.util.{ Map => JMap, List => JList, Collections => JCollections }
import com.umatrangolo.giltapi.model._
import scala.collection.JavaConverters._

import org.joda.time.DateTime

object TestUtils {
  val TestSale = Sale(
    null,
    "sale",
    "key",
    Store.Women,
    Some("description"),
    new URL("http://www.gilt.com"),
    new DateTime(),
    Some(new DateTime()),
    JCollections.emptyMap[ImageKey, JList[Image]],
    List(new URL("http://www.gilt.com/products/1"),
         new URL("http://www.gilt.com/products/2")).asJava)
}
