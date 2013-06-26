package com.umatrangolo.giltapi.core

import com.umatrangolo.giltapi.core.InventoryStatus._

case class Sku(
  id: Int,
  status: InventoryStatus,
  msrpPrice: Double,
  salePrice: Double,
  attributes: Map[String, Any] = Map.empty[String, Any]
)
