package com.umatrangolo.giltapi

import com.google.common.base.Optional
import com.google.common.util.concurrent.ListenableFuture

import com.umatrangolo.giltapi.model.Category
import com.umatrangolo.giltapi.model.Product
import com.umatrangolo.giltapi.model.Store._

import java.util.{ List => JList }

/**
 * Type used to fetch Products.
 *
 * An impl of this type will provide a non blocking client to fetch
 * Products from the GILT API endpoints. The implmentation is non blocking
 * and will always return a composable deferred result.
 */
trait Products {

  /**
   * Fetches a Product from its id.
   *
   * @param id unique identifier the Product.
   * @return a deferred optional Product.
   */
  def getProduct(id: Long): ListenableFuture[Optional[Product]]

  /**
   * Fetches all Product categories.
   *
   * Each Product belongs to one or more category. This endpoint fetches
   * all available categories from the GILT taxonomy.
   *
   * @return a deferred list of categories.
   */
  def getAllCategories: ListenableFuture[JList[Category]]
}
