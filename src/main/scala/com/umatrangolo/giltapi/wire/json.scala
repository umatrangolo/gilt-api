package com.umatrangolo.giltapi.wire.json

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule

import com.umatrangolo.giltapi.model.InventoryStatus
import com.umatrangolo.giltapi.model.Store
import com.umatrangolo.giltapi.model._
import com.umatrangolo.giltapi.wire.Deserializer

import java.net.MalformedURLException
import java.net.URL
import java.util.{ List => JList, Map => JMap, Collections => JCollections }

import org.joda.time.DateTime

import scala.beans.BeanProperty
import scala.collection.JavaConverters._
import scala.collection.LinearSeq
import scala.util.control.Exception._

// singleton JSON deserializer
private[wire] object JsonDeserializer extends Deserializer {

  private lazy val jsonMapper = {
    val mapper = new ObjectMapper()
    mapper.registerModule(new JodaModule())
    mapper
  }

  // TODO is possible to get rid of the casting ?
  override def deserialize[T: Manifest](bytes: Array[Byte]): T = manifest[T] match {
    case m if manifest[T] == manifest[Sale] => SaleJson.toSale(jsonMapper.readValue(bytes, classOf[SaleJson])).asInstanceOf[T]
    case m if manifest[T] == manifest[Product] => ProductJson.toProduct(jsonMapper.readValue(bytes, classOf[ProductJson])).asInstanceOf[T]
    case m if manifest[T] == manifest[LinearSeq[Sale]] => jsonMapper.readValue(bytes, classOf[SalesJson])
      .sales.asScala.map { SaleJson.toSale(_) }.toList.asInstanceOf[T]
    case m if manifest[T] == manifest[LinearSeq[Category]] => jsonMapper.readValue(bytes, classOf[CategoriesJson])
      .categories.asScala.map { CategoryJson.toCategory(_) }.toList.asInstanceOf[T]
    case _ => throw new RuntimeException("Type %s unsupported".format(manifest[T]))
  }
}

private[json] object StoreJson {
  private[this] val StoreMap = Store.values.map { s => (s.getKey -> s) }.toMap

  def toStore(storeJson: String): Store = Option(storeJson).flatMap { StoreMap.get(_) }.getOrElse {
    throw new RuntimeException("Unsupported store (was %s)".format(storeJson))
  }
}

private[json] object InventoryStatusJson {
  private[this] val InventoryStatusMap = InventoryStatus.values.map { s => (s.getKey -> s) }.toMap

  def toInventoryStatus(inventoryStatusJson: String): InventoryStatus = Option(inventoryStatusJson).flatMap { InventoryStatusMap.get(_) }
    .getOrElse { throw new RuntimeException("Unsupported inventory status (was %s)".format(inventoryStatusJson)) }
}

@JsonIgnoreProperties(ignoreUnknown = true)
private[json] final case class SaleJson(
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

private[json] object SaleJson {
  def toSale(saleJson: SaleJson): Sale = Sale(
    saleJson.name,
    saleJson.sale,
    saleJson.sale_key,
    StoreJson.toStore(saleJson.store),
    Option(saleJson.description),
    new URL(saleJson.sale_url),
    saleJson.begins,
    Option(saleJson.ends),
    JCollections.unmodifiableMap(saleJson.image_urls.asScala.map { case (imageKey, imageJsons) =>
      (ImageJson.toImageKey(imageKey) -> JCollections.unmodifiableList(imageJsons.asScala.map { imageJson =>
        catching(classOf[MalformedURLException]) opt ImageJson.toImage(imageJson) }.toList.flatten.asJava))
    }.toMap.asJava),
    Option(saleJson.products).map { ps =>
      JCollections.unmodifiableList(ps.asScala.toList.map {
        catching(classOf[MalformedURLException]) opt new URL(_)
      }.flatten.asJava)
    }.getOrElse(JCollections.emptyList[URL])
  )
}

@JsonIgnoreProperties(ignoreUnknown = true)
private[json] final case class SalesJson(
  @BeanProperty @JsonProperty("sales") sales: JList[SaleJson] = JCollections.emptyList[SaleJson]
)

@JsonIgnoreProperties(ignoreUnknown = true)
private[json] final case class CategoriesJson(
  @BeanProperty @JsonProperty("categories") categories: JList[String] = JCollections.emptyList[String]
)

private[json] object CategoryJson {
  def toCategory(key: String) = Category(key)
}

@JsonIgnoreProperties(ignoreUnknown = true)
private[json] final case class ImageJson(
  @BeanProperty @JsonProperty("url") url: String,
  @BeanProperty @JsonProperty("width") width: Int,
  @BeanProperty @JsonProperty("height") height: Int
)

private[json] object ImageJson {
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

@JsonIgnoreProperties(ignoreUnknown = true)
private[json] final case class ProductJson(
  @BeanProperty @JsonProperty("name") name: String,
  @BeanProperty @JsonProperty("product") product: String,
  @BeanProperty @JsonProperty("id") id: Long,
  @BeanProperty @JsonProperty("brand") brand: String,
  @BeanProperty @JsonProperty("url") url: String,
  @BeanProperty @JsonProperty("image_urls") image_urls: JMap[String, JList[ImageJson]] = JCollections.emptyMap[String, JList[ImageJson]],
  @BeanProperty @JsonProperty("skus") skus: JList[SkuJson] = JCollections.emptyList[SkuJson],
  @BeanProperty @JsonProperty("categories") categories: JList[String] = JCollections.emptyList[String],
  @BeanProperty @JsonProperty("content") content: ContentJson
)

private[json] object ProductJson {
  def toProduct(productJson: ProductJson): Product = Product(
    id = productJson.id.toInt,
    name = productJson.name,
    product = new URL(productJson.url),
    brand = productJson.brand,
    content = ContentJson.toContent(productJson.content),
    images = Option(productJson.image_urls).map { urls =>
      urls.asScala.map { case (imageKey, imageJsons) =>
        (ImageJson.toImageKey(imageKey) -> imageJsons.asScala.map {
          imageJson => catching(classOf[MalformedURLException]) opt ImageJson.toImage(imageJson)
        }.toList.flatten)
      }.toMap
    }.getOrElse(Map.empty[ImageKey, List[Image]]),
    skus = Option(productJson.skus).map { ss => ss.asScala.map { skuJson => SkuJson.toSku(skuJson) }.toList }.getOrElse(LinearSeq.empty[Sku]),
    categories = Option(productJson.categories).map { cs => cs.asScala.map { Category(_) }.toList }.getOrElse(LinearSeq.empty[Category])
  )
}

@JsonIgnoreProperties(ignoreUnknown = true)
private[json] final case class SkuJson(
  @BeanProperty @JsonProperty("id") id: Int,
  @BeanProperty @JsonProperty("inventory_status") inventory_status: String,
  @BeanProperty @JsonProperty("msrp_price") msrp: String,
  @BeanProperty @JsonProperty("sale_price") sale: String,
  @BeanProperty @JsonProperty("shipping_surcharge") shipping_surcharge: String,
  @BeanProperty @JsonProperty("attributes") attributes: JList[SkuAttributeJson] = JCollections.emptyList[SkuAttributeJson]
)

private[json] object SkuJson {
  def toSku(skuJson: SkuJson): Sku = Sku(
    id = skuJson.id.toInt,
    status = InventoryStatusJson.toInventoryStatus(skuJson.inventory_status),
    msrpPrice = skuJson.msrp.toDouble,
    salePrice = skuJson.sale.toDouble,
    attributes = JCollections.unmodifiableList(skuJson.attributes.asScala.map { SkuAttributeJson.toSkuAttribute }.toList.asJava)
  )
}

@JsonIgnoreProperties(ignoreUnknown = true)
private[json] final case class SkuAttributeJson(
  @BeanProperty @JsonProperty("name") name: String,
  @BeanProperty @JsonProperty("value") value: Any
)

private[json] object SkuAttributeJson {
  def toSkuAttribute(skuAttributeJson: SkuAttributeJson): SkuAttribute = SkuAttribute(
    name = skuAttributeJson.name,
    value = skuAttributeJson.value
  )
}

@JsonIgnoreProperties(ignoreUnknown = true)
private[json] final case class ContentJson(
  @BeanProperty @JsonProperty("description") description: String,
  @BeanProperty @JsonProperty("fit_notes") fit_notes: String,
  @BeanProperty @JsonProperty("material") material: String,
  @BeanProperty @JsonProperty("care_instructions") care_instructions: String,
  @BeanProperty @JsonProperty("origin") origin: String
)

private[json] object ContentJson {
  def toContent(contentJson: ContentJson): Content = Content(
    description = Some(contentJson.description),
    fitNotes = Some(contentJson.fit_notes),
    material = Some(contentJson.material),
    careInstructions = Some(contentJson.care_instructions),
    origin = Some(contentJson.origin)
  )
}
