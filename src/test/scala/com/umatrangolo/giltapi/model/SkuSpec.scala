package com.umatrangolo.giltapi.model

import org.scalatest.WordSpec

class SkuSpec extends WordSpec {

  private val TestSku = Sku(1, InventoryStatus.SoldOut, 100, 101, Map.empty[String, Any])

  "A Sku" when {
    "being built" should {
      "reject a negative id" in { intercept[IllegalArgumentException] { TestSku.copy(id = -1) } }
      "reject a null status" in { intercept[IllegalArgumentException] { TestSku.copy(status = null) } }
      "reject a null attributes" in { intercept[IllegalArgumentException] { TestSku.copy(attributes = null) } }
      "reject a negative msrpPrice" in { intercept[IllegalArgumentException] { TestSku.copy(msrpPrice = -10) } }
      "reject a negative salePrice" in { intercept[IllegalArgumentException] { TestSku.copy(salePrice = -10) } }
    }
  }
}
