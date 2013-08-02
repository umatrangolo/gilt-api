package com.umatrangolo.giltapi.model

import com.umatrangolo.giltapi.TestUtils

import java.net.URL

import org.joda.time.DateTime

import org.scalatest.WordSpec

import scala.collection.LinearSeq

class SaleSpec extends WordSpec {

  private val Now = new DateTime()
  private val BeforeNow = new DateTime(Now.getMillis - 10000)
  private val AfterABit = new DateTime(Now.getMillis + 10000)

  private val TestSale = Sale("name", "sale", "key", Store.Women, Some("description"), new URL("http://www.gilt.com"), Now, Some(AfterABit),
                              Map.empty[ImageKey, List[Image]], List(new URL("http://www.gilt.com/products/1"), new URL("http://www.gilt.com/products/2")))

  "A Sale" when {
    "constructed" should {
      "reject a null name" in { intercept[IllegalArgumentException] { TestSale.copy(name = null) } }
      "reject an empty name" in { intercept[IllegalArgumentException] { TestSale.copy(name = "") } }
      "reject a null sale" in { intercept[IllegalArgumentException] { TestSale.copy(sale = null) } }
      "reject an empty sale" in { intercept[IllegalArgumentException] { TestSale.copy(sale = "") } }
      "reject a null key" in { intercept[IllegalArgumentException] { TestSale.copy(key = null) } }
      "reject an empty key" in { intercept[IllegalArgumentException] { TestSale.copy(key = "") } }
      "reject a null store" in { intercept[IllegalArgumentException] { TestSale.copy(store = null ) } }
      "reject a null description" in { intercept[IllegalArgumentException] { TestSale.copy(description = null) } }
      "reject a null url" in { intercept[IllegalArgumentException] { TestSale.copy(url = null) } }
      "reject a null begins" in { intercept[IllegalArgumentException] { TestSale.copy(begins = null) } }
      "reject a null ends" in { intercept[IllegalArgumentException] { TestSale.copy(ends = null) } }
      "reject a null images" in { intercept[IllegalArgumentException] { TestSale.copy(images = null) } }
      "reject a null products" in { intercept[IllegalArgumentException] { TestSale.copy(products = null) } }
      "reject an end time that is before the begins time" in { intercept[IllegalArgumentException] { TestSale.copy(begins = AfterABit, ends = Some(Now)) } }
    }
    "used" should {
      "be active if it has some products" in {
        val actual = TestSale.copy(begins = BeforeNow, ends = Some(AfterABit))
        assert(actual.isActive)
        assert(!actual.isUpcoming)
      }
      "be upcoming if it has not products" in {
        val actual = TestSale.copy(begins = AfterABit, ends = Some(new DateTime(Now.getMillis + 20000)), products = LinearSeq.empty[URL])
        assert(actual.isUpcoming)
        assert(!actual.isActive)
      }
    }
  }
}
