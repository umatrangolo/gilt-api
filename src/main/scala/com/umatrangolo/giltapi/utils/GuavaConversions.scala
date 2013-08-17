package com.umatrangolo.giltapi.utils

import com.google.common.base.Optional

import scala.language.implicitConversions

object GuavaConversions {
  implicit def guavaOptional2Option[T](optional: Optional[T]): Option[T] = if (optional.isPresent) Some(optional.get) else None
}
