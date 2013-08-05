package com.umatrangolo.giltapi.model

import org.scalatest.WordSpec
import java.net.URL

import scala.collection.LinearSeq

class ProductSpec extends WordSpec {
  private val TestContent = Content(Some("description"), Some("fit-notes"), Some("material"), Some("careInstructions"), Some("origin"))
  private val TestProduct = Product(1, "product", new URL("http://www.google.com"), "brand", TestContent, Map.empty[ImageKey, List[Image]],
                                    LinearSeq.empty[Sku])

  "A Product" when {
    "being built" should {
      "reject a negative id" in { intercept[IllegalArgumentException] { TestProduct.copy(id = -1) } }
      "reject a null name" in { intercept[IllegalArgumentException] { TestProduct.copy(name = null) } }
      "reject an empty name" in { intercept[IllegalArgumentException] { TestProduct.copy(name = "") } }
      "reject a null product" in { intercept[IllegalArgumentException] { TestProduct.copy(product = null) } }
      "reject a null brand" in { intercept[IllegalArgumentException] { TestProduct.copy(brand = null) } }
      "reject an empty brand" in { intercept[IllegalArgumentException] { TestProduct.copy(brand = "") } }
      "reject a null content" in { intercept[IllegalArgumentException] { TestProduct.copy(content = null) } }
      "reject a null images" in { intercept[IllegalArgumentException] { TestProduct.copy(images = null) } }
      "reject a null skus" in { intercept[IllegalArgumentException] { TestProduct.copy(skus = null) } }
      "reject a null categories" in { intercept[IllegalArgumentException] { TestProduct.copy(categories = null) } }
    }
  }
}
