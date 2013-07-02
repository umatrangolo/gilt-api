package com.umatrangolo.giltapi.model

import com.umatrangolo.giltapi.model.InventoryStatus._

case class Sku(
  id: Int,
  status: InventoryStatus,
  msrpPrice: Double,
  salePrice: Double,
  attributes: Map[String, Any] = Map.empty[String, Any]
)
