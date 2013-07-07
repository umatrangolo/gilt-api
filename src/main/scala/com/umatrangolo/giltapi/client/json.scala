package com.umatrangolo.giltapi.client.json

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import com.umatrangolo.giltapi.model.Store._
import com.umatrangolo.giltapi.model._

import java.net.MalformedURLException
import java.net.URL
import java.util.{ List => JList, Map => JMap, Collections => JCollections }

import org.joda.time.DateTime

import scala.beans.BeanProperty
import scala.collection.JavaConverters._
import scala.collection.LinearSeq

object StoreJson {
  private[this] val ValueSet = Store.values

  def toStore(storeJson: String): Store = Option(storeJson).flatMap { s => ValueSet.find { _.toString == s } }.getOrElse {
    throw new RuntimeException("Unsupported store (was %s)".format(storeJson))
  }
}

@JsonIgnoreProperties(ignoreUnknown = true)
final case class SaleJson(
  @BeanProperty @JsonProperty("name") name: String,
  @BeanProperty @JsonProperty("sale") sale: String,
  @BeanProperty @JsonProperty("sale_key") sale_key: String,
  @BeanProperty @JsonProperty("store") store: String,
  @BeanProperty @JsonProperty("description") description: String,
  @BeanProperty @JsonProperty("sale_url") sale_url: String,
  @BeanProperty @JsonProperty("begins") begins: DateTime,
  @BeanProperty @JsonProperty("ends") ends: DateTime,
  @BeanProperty @JsonProperty("image_urls") image_urls: JMap[String, JList[ImageJson]] = JCollections.emptyMap[String, JList[ImageJson]],
  @BeanProperty @JsonProperty("products") products: JList[String] = JCollections.emptyList[String]
)

object SaleJson {
  def toSale(saleJson: SaleJson): Sale = Sale(
    saleJson.name,
    saleJson.sale,
    saleJson.sale_key,
    StoreJson.toStore(saleJson.store),
    Option(saleJson.description),
    new URL(saleJson.sale_url),
    saleJson.begins,
    Option(saleJson.ends),
    saleJson.image_urls.asScala.map { case (imageKey, imageJsons) =>
      (ImageJson.toImageKey(imageKey) -> imageJsons.asScala.map { imageJson => ImageJson.toImage(imageJson) }.toList)
    }.toMap,
    Option(saleJson.products).map { ps => ps.asScala.toList.map { new URL(_) } }.getOrElse(LinearSeq.empty[URL])
  )
}

@JsonIgnoreProperties(ignoreUnknown = true)
final case class SalesJson(
  @BeanProperty @JsonProperty("sales") sales: JList[SaleJson] = JCollections.emptyList[SaleJson]
)

@JsonIgnoreProperties(ignoreUnknown = true)
final case class ImageJson(
  @BeanProperty @JsonProperty("url") url: String,
  @BeanProperty @JsonProperty("width") width: Int,
  @BeanProperty @JsonProperty("height") height: Int
)

object ImageJson {
  def toImage(imageJson: ImageJson): Image = Image(
    new URL(imageJson.url),
    imageJson.width,
    imageJson.height
  )

  def toImageKey(imageKeyJson: String): ImageKey = imageKeyJson.trim.split("x").toList match {
    case x :: y :: Nil => ImageKey(x.toInt, y.toInt)
    case _ => throw new RuntimeException("Unsupported ImageKey format (was %s)".format(imageKeyJson))
  }
}
