package com.umatrangolo.giltapi.api

import com.umatrangolo.giltapi.core.Product
import com.umatrangolo.giltapi.core.Store._

import scala.collection.LinearSeq

trait Products {

  def products(id: Long): Option[Product]

  def allCategories: LinearSeq[String]

}
