package com.umatrangolo.giltapi.client

import com.umatrangolo.giltapi.wire._
import com.umatrangolo.giltapi.client.ning._

private[client] abstract class Client(apiKey: String, deserializer: Deserializer) {
  require(apiKey != null, "The apiKey must be not null")
  require(apiKey.trim.size > 0, "The apiKey must be non empty")
  require(deserializer != null, "deserializer can't be null")
}

object SalesClient {
  def apply(apiKey: String) = new NingSalesClientImpl(apiKey, Deserializer.instance, NingProvider)
}

object ProductsClient {
  def apply(apiKey: String) = new NingProductsClientImpl(apiKey, Deserializer.instance, NingProvider)
}
