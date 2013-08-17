package com.umatrangolo.giltapi.utils

import com.google.common.util.concurrent.{ ListenableFuture => GuavaListenableFuture }

import com.ning.http.client.{ ListenableFuture => NingListenableFuture }

import java.util.concurrent.{ TimeUnit, Executor }

import scala.concurrent.{ Future => ScalaFuture }

import scala.language.implicitConversions

object FutureConversions {
  implicit def ningListenableFuture2GuavaListenableFuture[T](ningFuture: NingListenableFuture[T]): GuavaListenableFuture[T] = {
    new GuavaListenableFuture[T] {
      override def cancel(mayInterruptIfRunning: Boolean) = ningFuture.cancel(mayInterruptIfRunning)
      override def isCancelled = ningFuture.isCancelled
      override def isDone = ningFuture.isDone
      override def get = ningFuture.get
      override def get(timeout: Long, unit: TimeUnit) = ningFuture.get(timeout, unit)
      override def addListener(cmd: Runnable, exec: Executor) { ningFuture.addListener(cmd, exec) }
    }
  }

  // TODO conversion to GuavaFuture for Java interoperability
  implicit def guavaListenableFuture2ScalaFuture[T](guavaFuture: GuavaListenableFuture[T]): ScalaFuture[T] = ???
}
