package com.umatrangolo.giltapi.core

import com.umatrangolo.giltapi.core.Store._

import java.net.URL

import org.joda.time.DateTime

import scala.collection.LinearSeq

case class Sale(
  name: String,
  sale: String,
  key: String,
  store: Store,
  description: String,
  url: String,
  begins: DateTime,
  ends: DateTime,
  images: Map[ImageKey, Image] = Map.empty[ImageKey, Image],
  products: LinearSeq[URL] = LinearSeq.empty[URL]
)
