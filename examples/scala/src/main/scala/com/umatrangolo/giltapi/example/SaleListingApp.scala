package com.umatrangolo.giltapi.example

import com.google.common.base.Optional

import com.umatrangolo.giltapi.client.GiltClientFactory
import com.umatrangolo.giltapi.model._
import com.umatrangolo.giltapi.{ Sales, Products }

import java.util.{ List => JList }

import scala.collection.JavaConversions._
import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps

object SaleListingApp extends App {
  println("**** Fetching sale listing ... ")

  val salesClient = GiltClientFactory.newSalesClientInstance()
  val productClient = GiltClientFactory.newProductsClientInstance()

  // active sales
  println("======== ACTIVE SALES ========")
  val activeSales: JList[Sale] = salesClient.getActiveSales.get()
  activeSales.groupBy { _.store }.foreach { case (store, sales) =>
    println(">> Store: " + store)
    sales.foreach { sale => println(">> " + sale.name) }
  }

  // upcoming sales
  println("======== UPCOMING SALES ========")
  val upcomingSales: JList[Sale] = salesClient.getUpcomingSales.get()
  upcomingSales.groupBy { _.store }.foreach { case (store, sales) =>
    println(">> Store: " + store)
    sales.foreach { sale => println(">> " + sale.name) }
  }

  // details about a single sale in the Women store
  println("======== FIRST SALE FOR WOMEN ========")
  val salesForWomen: JList[Sale] = salesClient.getActiveSales(Store.Women).get()
  salesForWomen.headOption.foreach { sale =>
    println(">> Getting details about " + sale.name)
    val saleDetails = salesClient.getSale(sale.key, Store.Women).get()
    println(">> [%s] is \n%s".format(sale.key, saleDetails))
  }

  import com.umatrangolo.giltapi.utils.GuavaConversions._

  // browsing a product
  println("======== BROWSING PRODUCT ========")
  salesForWomen.headOption.foreach { sale =>
    println(">> last sale in the Women store is " + sale.name)
    sale.productIds.headOption.foreach { pid =>
      println("\n\n**** Getting info about Product with id: " + pid)
      val product: Optional[Product] = productClient.getProduct(pid).get()
      product.foreach { p => println("---- Product with id %s is: \n%s".format(pid, p)) }
    }
  }

  println("======== BYE ========")
}
