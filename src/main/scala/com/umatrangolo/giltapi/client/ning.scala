package com.umatrangolo.giltapi.client.ning

import com.google.common.base.Optional
import com.google.common.util.concurrent.{ ListenableFuture => GuavaListenableFuture }

import com.ning.http.client.AsyncCompletionHandler
import com.ning.http.client.AsyncHttpClient
import com.ning.http.client.AsyncHttpClientConfig._
import com.ning.http.client.HttpResponseStatus
import com.ning.http.client.{ ListenableFuture => NingListenableFuture }
import com.ning.http.client.Response

import com.umatrangolo.giltapi.client.Client
import com.umatrangolo.giltapi.model.Store
import com.umatrangolo.giltapi.model._
import com.umatrangolo.giltapi.wire._
import com.umatrangolo.giltapi.{ Sales, Products }

import java.io.IOException
import java.util.Collections._
import java.util.{ Properties, List => JList }

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.collection.LinearSeq
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control.Exception._

private[ning] object NingSalesClientImpl {
  val logger = LoggerFactory.getLogger(classOf[NingSalesClientImpl])
}

private[client] trait NingProvider {
  def asyncClient: AsyncHttpClient
}

private[client] object NingProvider extends NingProvider {
  val logger = LoggerFactory.getLogger(classOf[NingProvider])

  private val default: Properties = {
    val props = new Properties()

    props.setProperty("giltapi.client.ning.is_compression_enabled", "true")
    props.setProperty("giltapi.client.ning.is_pooling_enabled", "true")
    props.setProperty("giltapi.client.ning.request_timeout_in_ms", "30000")
    props
  }

  private val properties = {
    val props = new Properties(default)
    val propIs = catching(classOf[IOException]) opt this.getClass.getClassLoader.getResourceAsStream("giltapi.properties")

    propIs.foreach { ps =>
      logger.info("Loading configuration from giltapi.properties found in the classpath")
      props.load(ps)
      ps.close
    }

    logger.debug("Properties are:\n" + props.toString)
    props
  }

  val isCompressionEnabled: Boolean = properties.getProperty("is_compression_enabled", "true").toBoolean
  val isPoolingConnectionEnabled: Boolean = properties.getProperty("is_pooling_enabled", "true").toBoolean
  val requestTimeoutInMs: Int = properties.getProperty("request_timeout_in_ms", "30000").toInt

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
  import com.umatrangolo.giltapi.utils.FutureConversions._

  logger.info("Ning Sales client impl created for api key: %s using provider: %s".format(apiKey, provider.asyncClient))

  override def getActiveSales: GuavaListenableFuture[JList[Sale]] = fetchActiveSales(None)

  override def getActiveSales(store: Store): GuavaListenableFuture[JList[Sale]] = fetchActiveSales(Option(store))

  override def getUpcomingSales: GuavaListenableFuture[JList[Sale]] = fetchUpcomingSales(None)

  override def getUpcomingSales(store: Store): GuavaListenableFuture[JList[Sale]] = fetchUpcomingSales(Option(store))

  override def getSale(saleKey: String, store: Store): GuavaListenableFuture[Optional[Sale]] = {
    val request = "https://api.gilt.com/v1/sales/%s/%s/detail.json?apikey=%s".format(store.getKey, saleKey, apiKey)

    provider.asyncClient.prepareGet(request.toString).execute(new AsyncCompletionHandlerImplWithStdErrorHandling[Optional[Sale]](
      on200 = { r =>
        try {
          Optional.of(deserializer.deserialize[Sale](r.getResponseBodyAsBytes()))
        } catch {
          case e: Exception => throw new RuntimeException("Error while deserializing service response. Was:\nRequest:%s\nResponse:%s\n"
            .format(request.toString, r.getResponseBody), e)
        }
      },
      on404 = { r => Optional.absent[Sale] }
    ))
  }

  protected def fetchSales(upcoming: Boolean = false)(store: Option[Store] = None): GuavaListenableFuture[JList[Sale]] = {
    val request = new StringBuilder("https://api.gilt.com/v1/sales/")

    store.foreach { s => request.append(s.getKey) }
    request.append("/").append({if (upcoming) "upcoming" else "active"})

    request.append(".json?apikey=%s".format(apiKey))

    provider.asyncClient.prepareGet(request.toString).execute(new AsyncCompletionHandlerImplWithStdErrorHandling[JList[Sale]](
      on200 = { r =>
        try {
          unmodifiableList(deserializer.deserialize[LinearSeq[Sale]](r.getResponseBodyAsBytes()).asJava)
        } catch {
          case e: Exception => throw new RuntimeException("Error while deserializing service response. Was:\nRequest:%s\nResponse:%s\n"
            .format(request.toString, r.getResponseBody), e)
        }
      },
      on404 = { r => emptyList[Sale] }
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
  import com.umatrangolo.giltapi.utils.FutureConversions._

  logger.info("Ning Products client impl created for api key: %s using provider: %s".format(apiKey, provider.asyncClient))

  override def getAllCategories: GuavaListenableFuture[JList[Category]] = {
    val request = "https://api.gilt.com/v1/products/categories.json?apikey=%s".format(apiKey)

    provider.asyncClient.prepareGet(request.toString).execute(new AsyncCompletionHandlerImplWithStdErrorHandling[JList[Category]](
      on200 = { r =>
        try {
          unmodifiableList(deserializer.deserialize[LinearSeq[Category]](r.getResponseBodyAsBytes()).asJava)
        } catch {
          case e: Exception => throw new RuntimeException("Error while deserializing service response. Was:\nRequest:%s\nResponse:%s\n"
            .format(request.toString, r.getResponseBody), e)
        }
      },
      on404 = { r => emptyList[Category] }
    ))
  }

  override def getProduct(id: Long): GuavaListenableFuture[Optional[Product]] = {
    val request = "https://api.gilt.com/v1/products/%s/detail.json?apikey=%s".format(id, apiKey)

    provider.asyncClient.prepareGet(request.toString).execute(new AsyncCompletionHandlerImplWithStdErrorHandling[Optional[Product]](
      on200 = { r =>
        try {
          Optional.of(deserializer.deserialize[Product](r.getResponseBodyAsBytes))
        } catch {
          case e: Exception => throw new RuntimeException("Error while deserializing service response. Was:\nRequest:%s\nResponse:%s\n"
            .format(request.toString, r.getResponseBody), e)
        }
      },
      on404 = { r => Optional.absent[Product] }
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
