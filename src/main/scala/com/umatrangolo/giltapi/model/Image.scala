package com.umatrangolo.giltapi.model

import java.net.URL

import scala.beans.BeanProperty

/** Key identifying an Image by its size */
case class ImageKey(
  @BeanProperty width: Int,
  @BeanProperty height: Int
) {
  require(width >= 0, "width can't be negative")
  require(height >= 0, "height can't be negative")
}

/** An Image of a Product */
case class Image(
  @BeanProperty url: URL,
  @BeanProperty width: Int,
  @BeanProperty height: Int
) {
  require(url != null, "url can't be null")
  require(width >= 0, "width can't be negative")
  require(height >= 0, "height can't be negative")
}
