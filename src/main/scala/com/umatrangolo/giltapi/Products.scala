package com.umatrangolo.giltapi

import com.umatrangolo.giltapi.model.Product
import com.umatrangolo.giltapi.model.Store._

import scala.collection.LinearSeq
import scala.concurrent.Future

trait Products {

  def products(id: Long): Future[Option[Product]]

  def allCategories: Future[LinearSeq[String]]

}
