package com.umatrangolo.giltapi

import java.net.URL
import com.umatrangolo.giltapi.model._

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
    Map.empty[ImageKey, List[Image]],
    List(new URL("http://www.gilt.com/products/1"),
         new URL("http://www.gilt.com/products/2")))
}
