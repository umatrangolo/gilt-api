package com.umatrangolo.giltapi.model

case class Category(key: String) {
  require(key != null, "key can't be null")
  require(key.trim.size > 0, "key must be non empty")
}
