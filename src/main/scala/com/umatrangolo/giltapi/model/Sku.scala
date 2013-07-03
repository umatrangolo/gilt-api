package com.umatrangolo.giltapi.model

import com.umatrangolo.giltapi.model.InventoryStatus._

case class Sku(
  id: Int,
  status: InventoryStatus,
  msrpPrice: Double,
  salePrice: Double,
  attributes: Map[String, Any] = Map.empty[String, Any]
) {
  require(id >= 0, "id can't be negative")
  require(status != null, "status can't be null")
  require(attributes != null, "attributes can't be null")
}
