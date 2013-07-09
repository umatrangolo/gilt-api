package com.umatrangolo.giltapi.wire

import com.umatrangolo.giltapi.wire.json._

// generic type used for orthogonalize deserialization of GILT objects from the wire
trait Deserializer {
  def deserialize[T: Manifest](bytes: Array[Byte]): T
}

object Deserializer {
  lazy val instance = JsonDeserializer
}
