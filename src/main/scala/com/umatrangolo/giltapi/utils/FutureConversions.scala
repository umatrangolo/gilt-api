package com.umatrangolo.giltapi.client.utils

import com.google.common.util.concurrent.{ ListenableFuture => GuavaListenableFuture }

import com.ning.http.client.{ ListenableFuture => NingListenableFuture }

import scala.concurrent.Future

object FutureConversions {
  implicit def ningListenableFuture2ScalaFuture[T](ningFuture: NingListenableFuture[T]): Future[T] = ???
  implicit def ningListenableFuture2GuavaFuture[T](ningFuture: NingListenableFuture[T]): GuavaListenableFuture[T] = ???
}
