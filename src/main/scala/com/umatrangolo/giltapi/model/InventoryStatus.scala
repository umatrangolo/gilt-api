package com.umatrangolo.giltapi.model

/**
 * Status of a Sku:
 * - Sold Out: no more inventory for this Sku
 * - For Sale: some still available
 * - Reserved: still available but reserved (10 mins window)
 */
object InventoryStatus extends Enumeration {
  type InventoryStatus = Value

  val SoldOut = Value("sold out")
  val ForSale = Value("for sale")
  val Reserved = Value("reserved")
}
