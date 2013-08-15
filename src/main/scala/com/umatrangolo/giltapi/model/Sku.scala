package com.umatrangolo.giltapi.model

import com.umatrangolo.giltapi.model.InventoryStatus._

import java.util.{ List => JList, Collections => JCollections }

import scala.beans.BeanProperty

/**
 * The real thing.
 *
 * This is what people buys (e.g. `Hermes bag` [Product] in `red` [SkuAttribute]).
 */
case class Sku(
  @BeanProperty id: Int,
  @BeanProperty status: InventoryStatus,
  @BeanProperty msrpPrice: Double,
  @BeanProperty salePrice: Double,
  @BeanProperty attributes: JList[SkuAttribute] = JCollections.emptyList[SkuAttribute]
) {
  require(id >= 0, "id can't be negative")
  require(status != null, "status can't be null")
  require(attributes != null, "attributes can't be null")
  require(msrpPrice >= 0, "msrp price can't be negative")
  require(salePrice >= 0, "sale price can't be negative")
}

/** An attribute of a Sku like `size`, `color`, `fabric`, etc. */
case class SkuAttribute(@BeanProperty name: String, @BeanProperty value: Any) {
  require(name != null, "name can't be null")
  require(value != null, "value can't be null")
}
