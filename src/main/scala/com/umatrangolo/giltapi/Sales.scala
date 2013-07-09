package com.umatrangolo.giltapi

import com.umatrangolo.giltapi.model.Sale
import com.umatrangolo.giltapi.model.Store._

import scala.collection.LinearSeq
import scala.concurrent.Future

/**
 * Main type to fetch active/upcoming sales.
 *
 * This type makes possible to fetch different types of sales from the
 * GILT API endpoints. The implementation is non-blocking and will
 * always returns back a future of the expected result.
 */
trait Sales {

  /**
   * Fetches active and upcoming sales.
   *
   * @return a deferred list of all the active and upcoming sales.
   */
  def activeSales: Future[LinearSeq[Sale]]

  /**
   * Fetched active and upcoming sales for a given store.
   *
   * @param store the store to fetch sales for.
   * @return a deferred list of all the active and upcoming sales.
   */
  def activeSales(store: Store): Future[LinearSeq[Sale]]

  /**
   * Fetches only upcoming sales.
   *
   * @return a deferred list of all the active and upcoming sales.
   */
  def upcomingSales: Future[LinearSeq[Sale]]

  /**
   * Fetches only upcoming sales for the given store.
   *
   * @return a deferred list with all the upcoming sales only.
   */
  def upcomingSales(store: Store): Future[LinearSeq[Sale]]

  /**
   * Retrieves details about a given sale.
   *
   * @param saleKey the uniquely identifying key of the sale
   * @return a deferred optional sale.
   */
  def sale(saleKey: String, store: Store): Future[Option[Sale]]

}
