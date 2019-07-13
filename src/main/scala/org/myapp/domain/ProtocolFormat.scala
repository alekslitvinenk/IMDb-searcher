package org.myapp.domain

import org.myapp.domain.Protocol.TitleAkas

import scala.util.Try

object ProtocolFormat {
  implicit def titleAkasDecoder(source: String): TitleAkas = {
    val components = source.split("\t")

    TitleAkas(
      titleId = components(0),
      ordering = components(1).toInt,
      title = components(2),
      region = components(3),
      language = components(4),
      types = components(5),
      attributes = components(6),
      isOriginalTitle = Try(components(7).toInt == 1).getOrElse(false),
    )
  }
}
