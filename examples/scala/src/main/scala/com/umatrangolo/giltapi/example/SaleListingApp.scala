package com.umatrangolo.giltapi.example

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
  val activeSales: JList[Sale] = Await.result(salesClient.getActiveSales, 30 seconds)
  activeSales.groupBy { _.store }.foreach { case (store, sales) =>
    println(">> Store: " + store)
    sales.foreach { sale => println(">> " + sale.name) }
  }

  // upcoming sales
  println("======== UPCOMING SALES ========")
  val upcomingSales: JList[Sale] = Await.result(salesClient.getUpcomingSales, 30 seconds)
  upcomingSales.groupBy { _.store }.foreach { case (store, sales) =>
    println(">> Store: " + store)
    sales.foreach { sale => println(">> " + sale.name) }
  }

  // details about a single sale in the Women store
  println("======== FIRST SALE FOR WOMEN ========")
  val salesForWomen: JList[Sale] = Await.result(salesClient.getActiveSales(Store.Women), 30 seconds)
  salesForWomen.headOption.foreach { sale =>
    println(">> Getting details about " + sale.name)
    val saleDetails = Await.result(salesClient.getSale(sale.key, Store.Women), 30 seconds)
    println(">> [%s] is \n%s".format(sale.key, saleDetails))
  }

  // browsing a product
  println("======== BROWSING PRODUCT ========")
  salesForWomen.headOption.foreach { sale =>
    println(">> last sale in the Women store is " + sale.name)
    sale.productIds.headOption.foreach { pid =>
      println("\n\n**** Getting info about Product with id: " + pid)
      val product = Await.result(productClient.getProduct(pid), 30 seconds)
      println("---- Product with id %s is: \n%s".format(pid, product))
    }
  }

  println("======== BYE ========")
}
