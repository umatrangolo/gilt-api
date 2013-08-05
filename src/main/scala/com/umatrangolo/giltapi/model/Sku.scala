package com.umatrangolo.giltapi.model

import com.umatrangolo.giltapi.model.InventoryStatus._

case class Sku(
  id: Int,
  status: InventoryStatus,
  msrpPrice: Double,
  salePrice: Double,
  attributes: List[SkuAttribute] = List.empty[SkuAttribute]
) {
  require(id >= 0, "id can't be negative")
  require(status != null, "status can't be null")
  require(attributes != null, "attributes can't be null")
  require(msrpPrice >= 0, "msrp price can't be negative")
  require(salePrice >= 0, "sale price can't be negative")
}

case class SkuAttribute(name: String, value: Any) {
  require(name != null, "name can't be null")
  require(value != null, "value can't be null")
}
