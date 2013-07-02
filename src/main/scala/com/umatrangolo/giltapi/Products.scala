package com.umatrangolo.giltapi

import com.umatrangolo.giltapi.model.Product
import com.umatrangolo.giltapi.model.Store._

import scala.collection.LinearSeq

trait Products {

  def products(id: Long): Option[Product]

  def allCategories: LinearSeq[String]

}
