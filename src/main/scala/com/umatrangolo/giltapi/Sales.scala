package com.umatrangolo.giltapi

import com.google.common.base.Optional
import com.google.common.util.concurrent.ListenableFuture

import com.umatrangolo.giltapi.model.Sale
import com.umatrangolo.giltapi.model.Store

import java.util.{ List => JList }

/**
 * Main type to fetch active/upcoming Sales.
 *
 * This type makes possible to fetch different types of sales from the
 * GILT API endpoints. The implementation is non-blocking and will
 * always returns back a future of the expected result.
 */
trait Sales {

  /**
   * Fetches active and upcoming Sales.
   *
   * @return a deferred list of all the active Sales.
   */
  def getActiveSales: ListenableFuture[JList[Sale]]

  /**
   * Fetched active and upcoming Sales for a given store.
   *
   * @param store the store to fetch Sales for.
   * @return a deferred list of all the active Sales for the store.
   */
  def getActiveSales(store: Store): ListenableFuture[JList[Sale]]

  /**
   * Fetches only upcoming Sales.
   *
   * @return a deferred list of all the upcoming Sales.
   */
  def getUpcomingSales: ListenableFuture[JList[Sale]]

  /**
   * Fetches only upcoming Sales for the given store.
   *
   * @return a deferred list with all upcoming Sales only for the store.
   */
  def getUpcomingSales(store: Store): ListenableFuture[JList[Sale]]

  /**
   * Retrieves details about a given Sale.
   *
   * @param saleKey the uniquely identifying key of the Sale
   * @return a deferred optional Sale.
   */
  def getSale(saleKey: String, store: Store): ListenableFuture[Optional[Sale]]
}
