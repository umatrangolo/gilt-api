package com.umatrangolo.giltapi.client.ning

import com.ning.http.client.AsyncCompletionHandler
import com.ning.http.client.AsyncHttpClient
import com.ning.http.client.AsyncHttpClientConfig._
import com.ning.http.client.HttpResponseStatus
import com.ning.http.client.ListenableFuture
import com.ning.http.client.Response

import com.umatrangolo.giltapi.client.Client
import com.umatrangolo.giltapi.model.Store._
import com.umatrangolo.giltapi.model._
import com.umatrangolo.giltapi.wire._
import com.umatrangolo.giltapi.{ Sales, Products }

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.collection.LinearSeq
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

private[ning] object NingSalesClientImpl {
  val logger = LoggerFactory.getLogger(classOf[NingSalesClientImpl])
}

private[client] trait NingProvider {
  def asyncClient: AsyncHttpClient
}

// TODO read from external config
private[client] object NingProvider extends NingProvider {
  lazy val isCompressionEnabled: Boolean = true
  lazy val isPoolingConnectionEnabled: Boolean = true
  lazy val requestTimeoutInMs: Int = 30000 // ms

  override val asyncClient = new AsyncHttpClient(
    new Builder()
    .setCompressionEnabled(isCompressionEnabled)
    .setAllowPoolingConnection(isPoolingConnectionEnabled)
    .setRequestTimeoutInMs(requestTimeoutInMs)
    .build()
  )

  override def toString: String =
    """
     |Ning Config is:
     | - is compression enabled  : %s
     | - is pooling enabled      : %s
     | - requestTimeoutInMs      : %s ms.
    """.format(isCompressionEnabled, isPoolingConnectionEnabled, requestTimeoutInMs).stripMargin
}

private[client] class NingSalesClientImpl(apiKey: String, deserializer: Deserializer, provider: NingProvider) extends Client(apiKey, deserializer) with Sales {
  import NingSalesClientImpl._
  import com.umatrangolo.giltapi.client.utils.FutureConversions._

  logger.info("Ning Sales client impl created for api key: %s using provider: %s".format(apiKey, provider.asyncClient))

  override def activeSales: Future[LinearSeq[Sale]] = fetchActiveSales(None)

  override def activeSales(store: Store): Future[LinearSeq[Sale]] = fetchActiveSales(Option(store))

  override def upcomingSales: Future[LinearSeq[Sale]] = fetchUpcomingSales(None)

  override def upcomingSales(store: Store): Future[LinearSeq[Sale]] = fetchUpcomingSales(Option(store))

  override def sale(saleKey: String, store: Store): Future[Option[Sale]] = {
    val request = new StringBuilder("https://api.gilt.com/v1/")
      .append(store).append("/").append(saleKey).append("/detail.json?apikey=%s".format(apiKey))

    provider.asyncClient.prepareGet(request.toString).execute(new AsyncCompletionHandlerImplWithStdErrorHandling[Option[Sale]](
      on200 = { r =>
        try {
          Option(deserializer.deserialize[Sale](r.getResponseBodyAsBytes()))
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

    provider.asyncClient.prepareGet(request.toString).execute(new AsyncCompletionHandlerImplWithStdErrorHandling[LinearSeq[Sale]](
      on200 = { r =>
        try {
          deserializer.deserialize[LinearSeq[Sale]](r.getResponseBodyAsBytes())
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
}

private[client] object NingProductsClientImpl {
  val logger = LoggerFactory.getLogger(classOf[NingProductsClientImpl])
}

private[client] class NingProductsClientImpl(apiKey: String, deserializer: Deserializer, provider: NingProvider)
                extends Client(apiKey, deserializer) with Products {
  import NingProductsClientImpl._
  import com.umatrangolo.giltapi.client.utils.FutureConversions._

  logger.info("Ning Products client impl created for api key: %s using provider: %s".format(apiKey, provider.asyncClient))

  override def allCategories: Future[LinearSeq[Category]] = {
    val request = "https://api.gilt.com/v1/products/categories.json?apikey=%s".format(apiKey)

    provider.asyncClient.prepareGet(request.toString).execute(new AsyncCompletionHandlerImplWithStdErrorHandling[LinearSeq[Category]](
      on200 = { r =>
        try {
          deserializer.deserialize[LinearSeq[Category]](r.getResponseBodyAsBytes())
        } catch {
          case e: Exception => throw new RuntimeException("Error while deserializing service response. Was:\nRequest:%s\nResponse:%s\n"
                                                          .format(request.toString, r.getResponseBody), e)
        }
             },
      on404 = { r => List.empty[Category] }
    ))
  }

  override def products(id: Long): Future[Option[Product]] = {
    val request = "https://api.gilt.com/v1/products/%s/detail.json?apikey=%s".format(id, apiKey)

    provider.asyncClient.prepareGet(request.toString).execute(new AsyncCompletionHandlerImplWithStdErrorHandling[Option[Product]](
      on200 = { r =>
        try {
          Option(deserializer.deserialize[Product](r.getResponseBodyAsBytes))
        } catch {
          case e: Exception => throw new RuntimeException("Error while deserializing service response. Was:\nRequest:%s\nResponse:%s\n"
                                                          .format(request.toString, r.getResponseBody), e)
        }
             },
      on404 = { r => None }
    ))
  }
}

private[ning] class AsyncCompletionHandlerImpl[T](
  on200: (Response) => T,
  on401: (Response) => T,
  on404: (Response) => T,
  on500: (Response) => T,
  onUnrecognized: (Response) => T
) extends AsyncCompletionHandler[T] {
  override def onCompleted(response: Response): T = response.getStatusCode match {
    case 200 => on200(response)
    case 401 => on401(response)
    case 404 => on404(response)
    case 500 => on500(response)
    case _ => onUnrecognized(response)
  }
}

private[ning] class AsyncCompletionHandlerImplWithStdErrorHandling[T](
  on200: (Response) => T,
  on404: (Response) => T
) extends AsyncCompletionHandlerImpl[T](
  on200,
  on401 = { r => throw new RuntimeException("Unauthorized") },
  on404,
  on500 = { r => throw new RuntimeException("Server error") },
  onUnrecognized = { r => throw new RuntimeException("Unrecognized status (was %s)".format(r.getStatusCode)) }
)
