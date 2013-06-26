package com.umatrangolo.giltapi.core

import java.net.URL

import scala.collection.LinearSeq

case class Product(
  name: String,
  product: URL,
  brand: String,
  content: Product#Content,
  images: Map[ImageKey, Image] = Map.empty[ImageKey, Image],
  skus: LinearSeq[Sku] = LinearSeq.empty[Sku]
) {
  case class Content(
    description: String,
    fitNotes: String,
    material: String,
    careInstuctions: String,
    origin: String
  )
}
