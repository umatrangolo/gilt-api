package com.umatrangolo.giltapi.client

import com.ning.http.client.AsyncHttpClient
import com.ning.http.client.AsyncHttpClientConfig._
import com.ning.http.client.AsyncCompletionHandler
import com.ning.http.client.Response

import com.umatrangolo.giltapi.model.Sale
import com.umatrangolo.giltapi.model.Store._
import com.umatrangolo.giltapi.Sales
import com.umatrangolo.giltapi.client.json._
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.collection.LinearSeq
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule

import com.ning.http.client.ListenableFuture
import com.ning.http.client.HttpResponseStatus

object NingSalesClientImpl {
  private[NingSalesClientImpl] val logger = LoggerFactory.getLogger(getClass)
}

// TODO move out of here
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

// TODO move out of here
object Json {
  lazy val jsonMapper = {
    val mapper = new ObjectMapper()
    mapper.registerModule(new JodaModule())
    mapper
  }
}

// TODO Use a factory to build instances of this
class NingSalesClientImpl(apiKey: String, ningConfig: NingConfig) extends Sales {
  import NingSalesClientImpl._
  import com.umatrangolo.giltapi.client.utils.FutureConversions._
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

  override def activeSales: Future[LinearSeq[Sale]] = fetchActiveSales(None)

  override def activeSales(store: Store): Future[LinearSeq[Sale]] = fetchActiveSales(Option(store))

  override def upcomingSales: Future[LinearSeq[Sale]] = fetchUpcomingSales(None)

  override def upcomingSales(store: Store): Future[LinearSeq[Sale]] = fetchUpcomingSales(Option(store))

  override def sale(saleKey: String, store: Store): Future[Option[Sale]] = {
    val request = new StringBuilder("https://api.gilt.com/v1/")
      .append(store).append("/").append(saleKey).append("/detail.json?apikey=%s".format(apiKey))

    asyncClient.prepareGet(request.toString).execute(new AsyncCompletionHandlerImplWithStdErrorHandling[Option[Sale]](
      on200 = { r =>
        try {
          val saleJson: SaleJson = jsonMapper.readValue(r.getResponseBodyAsBytes(), classOf[SaleJson])
          Option(SaleJson.toSale(saleJson))
        } catch {
          case e: Exception => throw new RuntimeException("Error while deserializing service response. Was:\nRequest:%s\nResponse:%s\n"
            .format(request.toString, r.getResponseBody), e)
        }
      },
      on404 = { r => None }
    ))
  }

  protected def fetchSales(upcoming: Boolean = false)(store: Option[Store] = None): Future[LinearSeq[Sale]] = {
    val request = new StringBuilder("https://api.gilt.com/v1/sales/")

    store.foreach { s => request.append(s.toString) }
    request.append("/").append({if (upcoming) "upcoming" else "active"})

    request.append(".json?apikey=%s".format(apiKey))

    asyncClient.prepareGet(request.toString).execute(new AsyncCompletionHandlerImplWithStdErrorHandling[LinearSeq[Sale]](
      on200 = { r =>
        try {
          val salesJson: SalesJson = jsonMapper.readValue(r.getResponseBodyAsBytes(), classOf[SalesJson])
          salesJson.sales.asScala.map { SaleJson.toSale(_) }.toList
        } catch {
          case e: Exception => throw new RuntimeException("Error while deserializing service response. Was:\nRequest:%s\nResponse:%s\n"
            .format(request.toString, r.getResponseBody), e)
        }
      },
      on404 = { r => LinearSeq.empty[Sale]}
    ))
  }

  protected def fetchUpcomingSales = fetchSales(true) _

  protected def fetchActiveSales = fetchSales(false) _

  // TODO move out of here
  private class AsyncCompletionHandlerImpl[T](on200: (Response) => T, on401: (Response) => T, on404: (Response) => T,
    on500: (Response) => T, onUnrecognized: (Response) => T) extends AsyncCompletionHandler[T] {
    override def onCompleted(response: Response): T = response.getStatusCode match {
      case 200 => on200(response)
      case 401 => on401(response)
      case 404 => on404(response)
      case 500 => on500(response)
      case _ => onUnrecognized(response)
    }
  }

  private class AsyncCompletionHandlerImplWithStdErrorHandling[T](on200: (Response) => T, on404: (Response) => T)
    extends AsyncCompletionHandlerImpl[T](
      on200,
      on401 = { r => throw new GiltUnauthorizedException },
      on404,
      on500 = { r => throw new GiltInternalServerError },
      onUnrecognized = { r => throw new GiltUnknownServerStatus(r.getStatusCode) }
    )

  // TODO move out of here
  final class GiltUnauthorizedException extends RuntimeException("Unauthorized")
  final class GiltInternalServerError extends RuntimeException("Gilt Internal Server Error")
  final class GiltUnknownServerStatus(code: Int) extends RuntimeException("Unkown response code (was %s)".format(code))
}
