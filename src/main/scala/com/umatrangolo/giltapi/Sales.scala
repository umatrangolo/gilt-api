package com.umatrangolo.giltapi

import com.umatrangolo.giltapi.model.Sale
import com.umatrangolo.giltapi.model.Store._

import scala.collection.LinearSeq
import scala.concurrent.Future

trait Sales {

  def activeSales: Future[LinearSeq[Sale]]

  def activeSales(store: Store): Future[LinearSeq[Sale]]

  def upcomingSales: Future[LinearSeq[Sale]]

  def upcomingSales(store: Store): Future[LinearSeq[Sale]]

  def sale(sale: String, store: Store): Future[Option[Sale]]

}
