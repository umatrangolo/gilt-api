package com.umatrangolo.giltapi.client.json

import org.joda.time.DateTime
import java.util.{ List => JList, Collections => JCollections }
import java.net.URL
import java.net.MalformedURLException
import com.umatrangolo.giltapi.model._
import scala.beans.BeanProperty
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import com.umatrangolo.giltapi.model.Store._

private[json] object Errors {
  def notPresent(key: String) = throw new RuntimeException("Incorrect JSON: value for `%s` was not present".format(key))
  def unrecognized(key: String, value: String) = throw new RuntimeException("Incorrect JSON: unrecognized value for `%s` (was %s)".format(key, value))
}

import Errors._

object StoreJson {
  private[this] val ValueSet = Store.values

  def toStore(storeJson: String): Store = Option(storeJson).map { s =>
    ValueSet.find { _.toString == s }.getOrElse { unrecognized("store", s) }
  }.getOrElse {
    notPresent("store")
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
  @BeanProperty @JsonProperty("ends") ends: DateTime
//  @BeanProperty @JsonProperty("image_urls") image_urls: JMap[String, JList[]] = JCollections.emptyList[String],
//  @BeanProperty @JsonProperty("products") products: JList[String] = JCollections.emptyList[String]
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
    Option(saleJson.ends)
  )
}

final case class SalesJson(
  @BeanProperty @JsonProperty("sales") sales: JList[SaleJson] = JCollections.emptyList[SaleJson]
)
