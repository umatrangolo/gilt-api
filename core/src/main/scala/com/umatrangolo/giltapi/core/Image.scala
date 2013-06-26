package com.umatrangolo.giltapi.core

import java.net.URL

case class ImageKey(
  width: Int,
  height: Int
)

case class Image(
  url: URL,
  width: Int,
  height: Int
)
