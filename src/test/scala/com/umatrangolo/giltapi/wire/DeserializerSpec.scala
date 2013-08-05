package com.umatrangolo.giltapi.wire

import org.scalatest.WordSpec
import com.umatrangolo.giltapi.model._
import java.net.URL
import org.joda.time.{ DateTime, DateTimeZone }

abstract class DeserializerSpec extends WordSpec {

  val deserializer: Deserializer

  val TestSaleAsBytes: Array[Byte]
  val TestSale = Sale("Dark Denim & Tees", "https://api.gilt.com/v1/sales/men/armani-jeans-theme/detail.json","armani-jeans-theme",Store.Men,
                      Some("Whether you dress them up or down, dark jeans are a modern essential. But you didn’t need us to tell you that. So what can we tell you? How about this: This sale features not just any dark denim, but options from Armani Jeans, Wings + Horns, Naked & Famous, and more, plus tees and jackets to pair them with."),
                      new URL("http://www.gilt.com/sale/men/armani-jeans-theme?utm_medium=api&utm_campaign=HelloWorld&utm_source=salesapi"),
                      new DateTime("2013-08-02T16:00:00.000Z", DateTimeZone.UTC),
                      Some(new DateTime("2013-08-04T04:00:00.000Z", DateTimeZone.UTC)),
                      Map(
                        ImageKey(315,295) -> List(Image(new URL("http://cdn1.gilt.com/images/share/uploads/0000/0003/0066/300667726/orig.jpg"),315,295)),
                        ImageKey(161,110) -> List(Image(new URL("http://cdn1.gilt.com/images/share/uploads/0000/0003/0066/300667587/orig.jpg"),161,110))
                      ),
                      List(
                        new URL("https://api.gilt.com/v1/products/1019421280/detail.json"),
                        new URL("https://api.gilt.com/v1/products/1000047488/detail.json"),
                        new URL("https://api.gilt.com/v1/products/1019421270/detail.json"),
                        new URL("https://api.gilt.com/v1/products/1019421267/detail.json"),
                        new URL("https://api.gilt.com/v1/products/1019421269/detail.json"),
                        new URL("https://api.gilt.com/v1/products/168468263/detail.json"),
                        new URL("https://api.gilt.com/v1/products/1000447267/detail.json"),
                        new URL("https://api.gilt.com/v1/products/173188100/detail.json"),
                        new URL("https://api.gilt.com/v1/products/1019421272/detail.json"),
                        new URL("https://api.gilt.com/v1/products/168468265/detail.json"),
                        new URL("https://api.gilt.com/v1/products/1019421274/detail.json"),
                        new URL("https://api.gilt.com/v1/products/1007338857/detail.json")
                      )
                    )

  val TestProductAsBytes: Array[Byte]
  val TestProduct = Product(
    1005047249,
    "Hooded Jacket",
    new URL("http://www.gilt.com/sale/men/henleys-1736/product/1005047249-sundek-hooded-jacket?utm_medium=api&utm_campaign=HelloWorld&utm_source=salesapi"),
    "Sundek",
    Content(
      Some("Nylon two-tone windbreaker jacket  Hooded with drawstring closure  Banded cuffs  Lower zip pockets  Seamed hem  Zip front closure  Full mesh lining    Measurements:    Rise: 12.75\", leg opening: 24\", inseam: 4\" (taken from a size 32)"),
      Some(null),
      Some("99% polyamide, 1% polyurethane"),
      Some("Machine wash"),
      Some("Cambodia")
    ),
    Map(
      ImageKey(300,400) -> List(
        Image(new URL("http://cdn1.gilt.com/images/share/uploads/0000/0003/0018/300181526/300x400.jpg"),300,400),
        Image(new URL("http://cdn1.gilt.com/images/share/uploads/0000/0003/0018/300181313/300x400.jpg"),300,400),
        Image(new URL("http://cdn1.gilt.com/images/share/uploads/0000/0003/0018/300181514/300x400.jpg"),300,400)),
      ImageKey(91,121) -> List(
        Image(new URL("http://cdn1.gilt.com/images/share/uploads/0000/0003/0018/300181526/91x121.jpg"),91,121),
        Image(new URL("http://cdn1.gilt.com/images/share/uploads/0000/0003/0018/300181313/91x121.jpg"),91,121),
        Image(new URL("http://cdn1.gilt.com/images/share/uploads/0000/0003/0018/300181514/91x121.jpg"),91,121))
    ),
    List(
      Sku(2407966,InventoryStatus.ForSale,420.0,149.0,List(
        SkuAttribute("size","xs"), SkuAttribute("color","black white"))),
      Sku(2407967,InventoryStatus.ForSale,420.0,149.0,List(
        SkuAttribute("size","s"), SkuAttribute("color","black white"))),
      Sku(2407968,InventoryStatus.ForSale,420.0,149.0,List(
        SkuAttribute("size","m"), SkuAttribute("color","black white")))),
    List(
      Category("Sweaters & Hoodies"),
      Category("Men"),
      Category("Clothing"),
      Category("Hoodies"),
      Category("Hoodies"),
      Category("Sweaters & Hoodies"),
      Category("Clothing"))
  )

  "The Deserializer" should {
    "correctly deserialize a Sale" in {
      expect(TestSale) { deserializer.deserialize[Sale](TestSaleAsBytes) }
    }

    "correctly deserialize a Product" in {
      expect(TestProduct) { deserializer.deserialize[Product](TestProductAsBytes) }
    }

  }
}

import com.umatrangolo.giltapi.wire.json._

class JsonDeserializerSpec extends DeserializerSpec {
  override val deserializer = JsonDeserializer
  override val TestSaleAsBytes: Array[Byte] =
    """
|{
|  "name": "Dark Denim & Tees",
|  "sale": "https://api.gilt.com/v1/sales/men/armani-jeans-theme/detail.json",
|  "sale_key": "armani-jeans-theme",
|  "store": "men",
|  "sale_url": "http://www.gilt.com/sale/men/armani-jeans-theme?utm_medium=api&utm_campaign=HelloWorld&utm_source=salesapi",
|  "image_urls": {
|    "161x110": [
|      {
|        "url": "http://cdn1.gilt.com/images/share/uploads/0000/0003/0066/300667587/orig.jpg",
|        "width": 161,
|        "height": 110
|      }
|    ],
|    "315x295": [
|      {
|        "url": "http://cdn1.gilt.com/images/share/uploads/0000/0003/0066/300667726/orig.jpg",
|        "width": 315,
|        "height": 295
|      }
|    ]
|  },
|  "begins": "2013-08-02T16:00:00Z",
|  "ends": "2013-08-04T04:00:00Z",
|  "description": "Whether you dress them up or down, dark jeans are a modern essential. But you didn’t need us to tell you that. So what can we tell you? How about this: This sale features not just any dark denim, but options from Armani Jeans, Wings + Horns, Naked & Famous, and more, plus tees and jackets to pair them with.",
|  "products": [
|    "https://api.gilt.com/v1/products/1019421280/detail.json",
|    "https://api.gilt.com/v1/products/1000047488/detail.json",
|    "https://api.gilt.com/v1/products/1019421270/detail.json",
|    "https://api.gilt.com/v1/products/1019421267/detail.json",
|    "https://api.gilt.com/v1/products/1019421269/detail.json",
|    "https://api.gilt.com/v1/products/168468263/detail.json",
|    "https://api.gilt.com/v1/products/1000447267/detail.json",
|    "https://api.gilt.com/v1/products/173188100/detail.json",
|    "https://api.gilt.com/v1/products/1019421272/detail.json",
|    "https://api.gilt.com/v1/products/168468265/detail.json",
|    "https://api.gilt.com/v1/products/1019421274/detail.json",
|    "https://api.gilt.com/v1/products/1007338857/detail.json"
|  ],
|  "$$hashKey": "00F"
|}
    """.stripMargin.getBytes

  override val TestProductAsBytes: Array[Byte] = """
|{"name":"Hooded Jacket",
| "product":"https://api.gilt.com/v1/products/1005047249/detail.json",
| "id":1005047249,
| "brand":"Sundek",
| "url":"http://www.gilt.com/sale/men/henleys-1736/product/1005047249-sundek-hooded-jacket?utm_medium=api&utm_campaign=HelloWorld&utm_source=salesapi",
| "content":{
|   "description":"Nylon two-tone windbreaker jacket  Hooded with drawstring closure  Banded cuffs  Lower zip pockets  Seamed hem  Zip front closure  Full mesh lining    Measurements:    Rise: 12.75\", leg opening: 24\", inseam: 4\" (taken from a size 32)",
|   "material":"99% polyamide, 1% polyurethane",
|   "care_instructions":"Machine wash",
|   "origin":"Cambodia"
| },
| "image_urls":{
|   "91x121":[
|     {"url":"http://cdn1.gilt.com/images/share/uploads/0000/0003/0018/300181526/91x121.jpg","width":91,"height":121},
|     {"url":"http://cdn1.gilt.com/images/share/uploads/0000/0003/0018/300181313/91x121.jpg","width":91,"height":121},
|     {"url":"http://cdn1.gilt.com/images/share/uploads/0000/0003/0018/300181514/91x121.jpg","width":91,"height":121}
|   ],
|   "300x400":[
|     {"url":"http://cdn1.gilt.com/images/share/uploads/0000/0003/0018/300181526/300x400.jpg","width":300,"height":400},
|     {"url":"http://cdn1.gilt.com/images/share/uploads/0000/0003/0018/300181313/300x400.jpg","width":300,"height":400},
|     {"url":"http://cdn1.gilt.com/images/share/uploads/0000/0003/0018/300181514/300x400.jpg","width":300,"height":400}
|   ]
| },
| "skus":[
|   {"id":2407966,"inventory_status":"for sale","units_for_sale":1,"msrp_price":"420.00","sale_price":"149.00","attributes":[
|     {"name":"size","value":"xs"},
|     {"name":"color","value":"black white"}]
|   },
|   {"id":2407967,"inventory_status":"for sale","units_for_sale":2,"msrp_price":"420.00","sale_price":"149.00","attributes":[
|     {"name":"size","value":"s"},
|     {"name":"color","value":"black white"}]
|   },
|   {"id":2407968,"inventory_status":"for sale","units_for_sale":1,"msrp_price":"420.00","sale_price":"149.00","attributes":[
|     {"name":"size","value":"m"},
|     {"name":"color","value":"black white"}]
|   }
| ],
| "categories":["Sweaters & Hoodies","Men","Clothing","Hoodies","Hoodies","Sweaters & Hoodies","Clothing"]
|}""".stripMargin.getBytes
}
