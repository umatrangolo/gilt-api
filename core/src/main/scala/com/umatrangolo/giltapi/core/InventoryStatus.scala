package com.umatrangolo.giltapi.core

object InventoryStatus extends Enumeration {
  type InventoryStatus = Value
  val SoldOut, ForSale, Reserved = Value
}
