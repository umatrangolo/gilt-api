package com.umatrangolo.giltapi.client

import com.umatrangolo.giltapi.wire._
import com.umatrangolo.giltapi.client.ning._
import com.umatrangolo.giltapi.utils.ApiKey

private[client] abstract class Client(apiKey: String, deserializer: Deserializer) {
  require(apiKey != null, "The apiKey must be not null")
  require(apiKey.trim.size > 0, "The apiKey must be non empty")
  require(deserializer != null, "deserializer can't be null")
}

/**
 * Factory for all the available Gilt clients.
 */
object GiltClientFactory {

  /**
   * Returns a new instance of the Sales client fetching api key from
   * sys properties or giltapi.properties file in the classpath.
   */
  def newSalesClientInstance() = new NingSalesClientImpl(ApiKey.value.get, Deserializer.instance, NingProvider)

  /**
   * Returns a new instance of the Sales client fetching api key from
   * sys properties or giltapi.properties file in the classpath.
   */
  def newSalesClientInstance(apiKey: String) = new NingSalesClientImpl(apiKey, Deserializer.instance, NingProvider)

  /**
   * Returns a new instance of the Products client fetching api key from
   * sys properties or giltapi.properties file in the classpath.
   */
  def newProductsClientInstance() = new NingProductsClientImpl(ApiKey.value.get, Deserializer.instance, NingProvider)

  /**
   * Returns a new instance of the Products client fetching api key from
   * sys properties or giltapi.properties file in the classpath.
   */
  def newProductsClientInstnace(apiKey: String) = new NingProductsClientImpl(apiKey, Deserializer.instance, NingProvider)
}
