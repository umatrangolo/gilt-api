package com.umatrangolo.giltapi.utils

import com.google.common.util.concurrent.{ ListenableFuture => GuavaListenableFuture, MoreExecutors }

import com.ning.http.client.{ ListenableFuture => NingListenableFuture }

import java.lang.Runnable
import java.util.concurrent.{ TimeUnit, Executor }

import scala.concurrent.{ Future => ScalaFuture, Promise }
import scala.language.implicitConversions
import scala.util.Try

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

  implicit def guavaListenableFuture2ScalaFuture[T](guavaFuture: GuavaListenableFuture[T]): ScalaFuture[T] = {
    val p = Promise[T]()

    guavaFuture.addListener(
      new Runnable {
        override def run() {
          p.complete(Try(guavaFuture.get())) // this is already resolved
        }
      }, MoreExecutors.sameThreadExecutor)

    p.future
  }
}
