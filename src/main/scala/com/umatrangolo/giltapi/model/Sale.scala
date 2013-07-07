package com.umatrangolo.giltapi.model

import com.umatrangolo.giltapi.model.Store._

import java.net.URL

import org.joda.time.DateTime

import scala.collection.LinearSeq

case class Sale(
  name: String,
  sale: String,
  key: String,
  store: Store,
  description: Option[String] = None,
  url: URL,
  begins: DateTime,
  ends: Option[DateTime] = None,
  images: Map[ImageKey, List[Image]] = Map.empty[ImageKey, List[Image]],
  products: LinearSeq[URL] = LinearSeq.empty[URL]
) {
  require(name != null, "name can't be null")
  require(name.trim.size > 0, "name can'e be empty")
  require(sale != null, "sale can't be null")
  require(sale.trim.size > 0, "sale can'e be empty")
  require(key != null, "key can't be null")
  require(key.trim.size > 0, "key can'e be empty")
  require(store != null, "store can't be null")
  require(description != null, "description can't be null")
  require(url != null, "url can't be null")
  require(begins != null, "begins can't be null")
  require(ends != null, "ends can't be null")
  if (ends.isDefined) require(ends.get.isAfter(begins), "ends should be after begins")
  require(images != null, "images can't be null")
  require(products != null, "products can't be null")

  def isUpcoming = products.isEmpty
  def isActive = !isUpcoming
}
