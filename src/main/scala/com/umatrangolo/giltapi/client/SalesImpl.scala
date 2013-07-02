package com.umatrangolo.giltapi.client

import com.ning.http.client.AsyncHttpClient
import com.ning.http.client.AsyncHttpClientConfig._
import com.ning.http.client.AsyncCompletionHandler
import com.ning.http.client.Response

import com.umatrangolo.giltapi.Sales
import com.umatrangolo.giltapi.model.Sale
import com.umatrangolo.giltapi.model.Store._

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.collection.LinearSeq
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.fasterxml.jackson.databind.ObjectMapper

object NingSalesClientImpl {
  private[NingSalesClientImpl] val logger = LoggerFactory.getLogger(getClass)
}

case class NingConfig(
  isCompressionEnabled: Boolean = true,
  isPoolingConnectionEnabled: Boolean = true,
  requestTimeoutInMs: Int = 30000 // ms
) {
  override def toString: String =
    """
     |Ning Config is:
     | - is compression enabled  : %s
     | - is pooling enabled      : %s
     | - requestTimeoutInMs      : %s ms.
    """.format(isCompressionEnabled, isPoolingConnectionEnabled, requestTimeoutInMs).stripMargin
}

import scala.beans.BeanProperty

case class SalesJsonResponse(@BeanProperty sales: java.util.ArrayList[Sale])

object Json {
  lazy val jsonMapper = new ObjectMapper()
}

class NingSalesClientImpl(apiKey: String, ningConfig: NingConfig) extends Sales {
  import NingSalesClientImpl._
  import Json._

  require(apiKey != null, "The apiKey must be not null")
  require(apiKey.trim.size > 0, "The apiKey must be non empty")

  private[this] val asyncClient: AsyncHttpClient = new AsyncHttpClient(
    new Builder()
      .setCompressionEnabled(ningConfig.isCompressionEnabled)
      .setAllowPoolingConnection(ningConfig.isPoolingConnectionEnabled)
      .setRequestTimeoutInMs(ningConfig.requestTimeoutInMs)
      .build()
    )

  logger.info("Ning Sales client impl created for api key: %s and Ning config: %s".format(apiKey, ningConfig))

  override def activeSales: Future[LinearSeq[Sale]] = {
    asyncClient.prepareGet("https://api.gilt.com/v1/sales/active.json?apikey=%s".format(apiKey)).execute(new AsyncCompletionHandler[LinearSeq[Sale]]() {
      override def onCompleted(response: com.ning.http.client.Response): LinearSeq[com.umatrangolo.giltapi.model.Sale] = {
        val salesResponse: SalesJsonResponse = jsonMapper.readValue(response.getResponseBodyAsBytes(), classOf[SalesJsonResponse])
        salesResponse.sales.asScala.toList
      }
    })

    Future { LinearSeq.empty[Sale] }
  }

  override def activeSales(store: Store): Future[LinearSeq[Sale]] = ???

  override def upcomingSales: Future[LinearSeq[Sale]] = ???

  override def upcomingSales(store: Store): Future[LinearSeq[Sale]] = ???

  override def sale(sale: String, store: Store): Future[Option[Sale]] = ???

}
