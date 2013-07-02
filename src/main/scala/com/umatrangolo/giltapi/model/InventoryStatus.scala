package com.umatrangolo.giltapi.model

object InventoryStatus extends Enumeration {
  type InventoryStatus = Value

  val SoldOut = Value("sold out")
  val ForSale = Value("for sale")
  val Reserved = Value("reserved")
}
