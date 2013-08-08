package com.umatrangolo.giltapi.example

import com.umatrangolo.giltapi.{ Sales, Products }
import com.umatrangolo.giltapi.client.{ SalesClient, ProductsClient }
import com.umatrangolo.giltapi.model._

import scala.collection.LinearSeq
import scala.concurrent._
import scala.concurrent.duration._

object SaleListingApp extends App {
  println("**** Fetching sale listing ... ")

  val salesClient = SalesClient()
  val productClient = ProductsClient()

  // active sales
  println("======== ACTIVE SALES ========")
  val activeSales: LinearSeq[Sale] = Await.result(salesClient.activeSales, 30 seconds)
  activeSales.groupBy { _.store }.foreach { case (store, sales) =>
    println(">> Store: " + store)
    sales.foreach { sale => println(">> " + sale.name) }
  }

  // upcoming sales
  println("======== UPCOMING SALES ========")
  val upcomingSales: LinearSeq[Sale] = Await.result(salesClient.upcomingSales, 30 seconds)
  upcomingSales.groupBy { _.store }.foreach { case (store, sales) =>
    println(">> Store: " + store)
    sales.foreach { sale => println(">> " + sale.name) }
  }

  // details about a single sale in the Women store
  println("======== FIRST SALE FOR WOMEN ========")
  val salesForWomen: LinearSeq[Sale] = Await.result(salesClient.activeSales(Store.Women), 30 seconds)
  salesForWomen.headOption.foreach { sale =>
    println(">> Getting details about " + sale.name)
    val saleDetails = Await.result(salesClient.sales(sale.key, Store.Women), 30 seconds)
    println(">> [%s] is \n%s".format(sale.key, saleDetails))
  }

  // browsing a product
  println("======== BROWSING PRODUCT ========")
  salesForWomen.headOption.foreach { sale =>
    println(">> last sale in the Women store is " + sale.name)
    sale.productIds.headOption.foreach { pid =>
      println("\n\n**** Getting info about Product with id: " + pid)
      val product = Await.result(productClient.products(pid), 30 seconds)
      println("---- Product with id %s is: \n%s".format(pid, product))
    }
  }

  println("======== BYE ========")
}
