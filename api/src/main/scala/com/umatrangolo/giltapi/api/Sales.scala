package com.umatrangolo.giltapi.api

import scala.collection.LinearSeq
import com.umatrangolo.giltapi.core.Sale
import com.umatrangolo.giltapi.core.Store._

trait Sales {

  def activeSales(): LinearSeq[Sale]

  def activeSales(store: Store): LinearSeq[Sale]

}
