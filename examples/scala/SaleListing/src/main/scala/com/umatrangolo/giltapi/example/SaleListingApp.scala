package com.umatrangolo.giltapi.example

import com.google.common.base.Optional

import com.umatrangolo.giltapi.client.GiltClientFactory
import com.umatrangolo.giltapi.model._
import com.umatrangolo.giltapi.{ Sales, Products }
import com.umatrangolo.giltapi.utils.FutureConversions._
import com.umatrangolo.giltapi.utils.GuavaConversions._

import java.util.{ List => JList }

import scala.collection.JavaConversions._
import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps

import ExecutionContext.Implicits.global

object SaleListingApp extends App {
  val salesClient = GiltClientFactory.newSalesClientInstance()
  val productClient = GiltClientFactory.newProductsClientInstance()

  salesClient.getActiveSales(Store.Women).map { salesForWomen =>

    // details about a single sale in the Women store
    println("======== FIRST SALE FOR WOMEN ========")
    salesForWomen.headOption.foreach { sale =>
      println(">> Getting details about " + sale.name)
      salesClient.getSale(sale.key, Store.Women).map { saleDetails =>
        println(">> [%s] is \n%s".format(sale.key, saleDetails))
      }
    }

    // browsing a product
    println("======== BROWSING PRODUCT ========")
    salesForWomen.headOption.foreach { sale =>
      println(">> first sale in the Women store is " + sale.name)
      sale.productIds.headOption.foreach { pid =>
        println("\n\n**** Getting info about Product with id: " + pid)
        productClient.getProduct(pid).map { product =>
          product.foreach { p => println("---- Product with id %s is: \n%s".format(pid, p)) }
          println("======== BYE ========")
        }
      }
    }
  }
}
