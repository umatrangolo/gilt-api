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
  description: String,
  url: URL,
  begins: DateTime,
  ends: DateTime,
  images: Map[ImageKey, Image] = Map.empty[ImageKey, Image],
  products: LinearSeq[URL] = LinearSeq.empty[URL]
)
