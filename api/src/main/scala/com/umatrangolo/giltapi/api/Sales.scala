package com.umatrangolo.giltapi.api

import scala.collection.LinearSeq
import com.umatrangolo.giltapi.core.Sale
import com.umatrangolo.giltapi.core.Store._

trait Sales {

  def activeSales(): LinearSeq[Sale]

  def activeSales(store: Store): LinearSeq[Sale]

  def upcomingSales(): LinearSeq[Sale]

  def upcomingSales(store: Store): LinearSeq[Sale]

  def sale(sale: String, store: Store): Option[Sale]

}
