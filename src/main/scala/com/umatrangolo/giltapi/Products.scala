package com.umatrangolo.giltapi

import com.umatrangolo.giltapi.model.Category
import com.umatrangolo.giltapi.model.Product
import com.umatrangolo.giltapi.model.Store._

import scala.collection.LinearSeq
import scala.concurrent.Future

/**
 * Type used to fetch products.
 *
 * An impl of this type will provide a non blocking client to fetch
 * products from the GILT API endpoints.
 */
trait Products {

  /**
   * Fetches a product from its id.
   *
   * @param id unique identifier the product.
   * @return a deferred optional product.
   */
  def products(id: Long): Future[Option[Product]]

  /**
   * Fetches all product categories.
   *
   * Each product belongs to one or more category. This endpoint fetches
   * all available categories from the GILT taxonomy.
   *
   * @return a deferred list of categories.
   */
  def allCategories: Future[LinearSeq[Category]]
}
