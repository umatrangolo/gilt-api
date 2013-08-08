package com.umatrangolo.giltapi.model

import java.net.URL

import scala.beans.BeanProperty
import scala.collection.LinearSeq

/**
 * Something availble in a Sale.
 *
 * A Product is mainly a set of Images showing how it looks like and
 * a set of Skus keeping track of each different form you can find this
 * Product in (e.g. this Product in size X, XS, M, etc.).
 */
case class Product(
  @BeanProperty id: Int,
  @BeanProperty name: String,
  @BeanProperty product: URL,
  @BeanProperty brand: String,
  @BeanProperty content: Content,
  @BeanProperty images: Map[ImageKey, List[Image]] = Map.empty[ImageKey, List[Image]],
  @BeanProperty skus: LinearSeq[Sku] = LinearSeq.empty[Sku],
  @BeanProperty categories: LinearSeq[Category] = LinearSeq.empty[Category]
) {
  require(id >= 0, "id can't be negative")
  require(name != null, "name can't be null")
  require(name.trim.size > 0, "name can'e be empty")
  require(product != null, "product can't be null")
  require(brand != null, "brand can't be null")
  require(brand.trim.size > 0, "brand can'e be empty")
  require(content != null, "content can't be null")
  require(images != null, "images can't be null")
  require(skus != null, "skus can't be null")
  require(categories != null, "categories can't be null")
}

case class Content(
  @BeanProperty description: Option[String] = None,
  @BeanProperty fitNotes: Option[String] = None,
  @BeanProperty material: Option[String] = None,
  @BeanProperty careInstructions: Option[String] = None,
  @BeanProperty origin: Option[String] = None
) {
  require(description != null, "description can't be null")
  require(fitNotes != null, "fitNotes can't be null")
  require(material != null, "material can't be null")
  require(careInstructions != null, "careInstructions can't be null")
  require(origin != null, "origin can't be null")
}
