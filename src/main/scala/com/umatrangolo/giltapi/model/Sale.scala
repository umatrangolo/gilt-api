package com.umatrangolo.giltapi.model

import com.umatrangolo.giltapi.model.Store._

import java.net.URL
import java.util.{ List => JList, Map => JMap, Collections => JCollections }
import java.lang.{ Long => JLong }

import org.joda.time.DateTime

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

/** A set of Products available to sell at discounted prices */
case class Sale(
  @BeanProperty name: String,
  @BeanProperty sale: String,
  @BeanProperty key: String,
  @BeanProperty store: Store,
  @BeanProperty description: Option[String] = None,
  @BeanProperty url: URL,
  @BeanProperty begins: DateTime,
  @BeanProperty ends: Option[DateTime] = None,
  @BeanProperty images: JMap[ImageKey, JList[Image]] = JCollections.emptyMap[ImageKey, JList[Image]],
  @BeanProperty products: JList[URL] = JCollections.emptyList[URL]
) {
  require(name != null, "name can't be null")
  require(name.trim.size > 0, "name can'e be empty")
  require(sale != null, "sale can't be null")
  require(sale.trim.size > 0, "sale can'e be empty")
  require(key != null, "key can't be null")
  require(key.trim.size > 0, "key can'e be empty")
  require(store != null, "store can't be null")
  require(description != null, "description can't be null")
  require(url != null, "url can't be null")
  require(begins != null, "begins can't be null")
  require(ends != null, "ends can't be null")
  if (ends.isDefined) require(ends.get.isAfter(begins), "ends should be after begins")
  require(images != null, "images can't be null")
  require(products != null, "products can't be null")

  def isUpcoming = products.isEmpty
  def isActive = !isUpcoming

  @BeanProperty lazy val productIds: JList[JLong] = JCollections.unmodifiableList(products.asScala.map { url =>
    val split = url.toString.split("products/")(1)
    JLong.valueOf(split.substring(0, split.indexOf("/")))
  }.asJava)
}
