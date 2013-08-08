package com.umatrangolo.giltapi

import com.umatrangolo.giltapi.model.Sale
import com.umatrangolo.giltapi.model.Store._

import scala.collection.LinearSeq
import scala.concurrent.Future

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
  def activeSales: Future[LinearSeq[Sale]]

  /**
   * Fetched active and upcoming Sales for a given store.
   *
   * @param store the store to fetch Sales for.
   * @return a deferred list of all the active Sales for the store.
   */
  def activeSales(store: Store): Future[LinearSeq[Sale]]

  /**
   * Fetches only upcoming Sales.
   *
   * @return a deferred list of all the upcoming Sales.
   */
  def upcomingSales: Future[LinearSeq[Sale]]

  /**
   * Fetches only upcoming Sales for the given store.
   *
   * @return a deferred list with all upcoming Sales only for the store.
   */
  def upcomingSales(store: Store): Future[LinearSeq[Sale]]

  /**
   * Retrieves details about a given Sale.
   *
   * @param saleKey the uniquely identifying key of the Sale
   * @return a deferred optional Sale.
   */
  def sales(saleKey: String, store: Store): Future[Option[Sale]]
}
