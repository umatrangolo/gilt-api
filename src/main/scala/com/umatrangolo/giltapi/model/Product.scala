package com.umatrangolo.giltapi.model

import java.net.URL

import scala.collection.LinearSeq

case class Product(
  id: Int,
  name: String,
  product: URL,
  brand: String,
  content: Content,
  images: Map[ImageKey, List[Image]] = Map.empty[ImageKey, List[Image]],
  skus: LinearSeq[Sku] = LinearSeq.empty[Sku]
) {
  require(name != null, "name can't be null")
  require(name.trim.size > 0, "name can'e be empty")
  require(product != null, "product can't be null")
  require(brand != null, "brand can't be null")
  require(content != null, "content can't be null")
  require(images != null, "images can't be null")
  require(skus != null, "skus can't be null")
}

case class Content(
  description: String,
  fitNotes: String,
  material: String,
  careInstructions: String,
  origin: String
) {
  require(description != null, "description can't be null")
  require(fitNotes != null, "fitNotes can't be null")
  require(material != null, "material can't be null")
  require(careInstructions != null, "careInstructions can't be null")
  require(origin != null, "origin can't be null")
}
