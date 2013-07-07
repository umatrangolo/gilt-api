package com.umatrangolo.giltapi.model

import java.net.URL

case class ImageKey(
  width: Int,
  height: Int
) {
  require(width >= 0, "width can't be negative")
  require(height >= 0, "height can't be negative")
}

case class Image(
  url: URL,
  width: Int,
  height: Int
) {
  require(url != null, "url can't be null")
  require(width >= 0, "width can't be negative")
  require(height >= 0, "height can't be negative")
}
