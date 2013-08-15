package com.umatrangolo.giltapi

import com.umatrangolo.giltapi.model.Category
import com.umatrangolo.giltapi.model.Product
import com.umatrangolo.giltapi.model.Store._

import java.util.{ List => JList }

import scala.concurrent.Future

/**
 * Type used to fetch Products.
 *
 * An impl of this type will provide a non blocking client to fetch
 * Products from the GILT API endpoints.
 */
trait Products {

  /**
   * Fetches a Product from its id.
   *
   * @param id unique identifier the Product.
   * @return a deferred optional Product.
   */
  def getProduct(id: Long): Future[Option[Product]]

  /**
   * Fetches all Product categories.
   *
   * Each Product belongs to one or more category. This endpoint fetches
   * all available categories from the GILT taxonomy.
   *
   * @return a deferred list of categories.
   */
  def getAllCategories: Future[JList[Category]]
}
