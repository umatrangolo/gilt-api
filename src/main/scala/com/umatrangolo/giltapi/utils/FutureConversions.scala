package com.umatrangolo.giltapi.client.utils

import com.google.common.util.concurrent.{ ListenableFuture => GuavaListenableFuture }

import com.ning.http.client.{ ListenableFuture => NingListenableFuture }

import scala.concurrent.{ Future, Promise }

object FutureConversions {
  private[this] val executor = java.util.concurrent.Executors.newCachedThreadPool()

  implicit def ningListenableFuture2ScalaFuture[T](ningFuture: NingListenableFuture[T]): Future[T] = {
    val promise: Promise[T] = Promise[T]()

    ningFuture.addListener(new Runnable() {
      override def run() {
        try {
          // this is not going to block given that this runnable is invoked when the ning future is completed
          promise.success(ningFuture.get)
        } catch {
          case e: Exception =>
            promise.failure(e)
        }
      }

      promise.future
    }, executor)
  }

  implicit def ningListenableFuture2GuavaFuture[T](ningFuture: NingListenableFuture[T]): GuavaListenableFuture[T] = ???
}
