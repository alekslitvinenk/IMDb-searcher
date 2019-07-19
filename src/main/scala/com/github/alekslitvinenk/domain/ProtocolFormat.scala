package com.github.alekslitvinenk.domain

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.alekslitvinenk.domain.Protocol._
import spray.json.DefaultJsonProtocol

import scala.util.Try

object ProtocolFormat {

  trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val searchFormat = jsonFormat8(JoitSearhResult)
    implicit val itemFormat = jsonFormat1(SearchResult)
  }

  def primaryTitleIndexDecoder(source: String): PrimaryTitleIndex = {
    val components = source.split("\t")

    PrimaryTitleIndex(
      tconst = components(0),
      primaryTitle = components(2),
    )
  }

  def titleBasicsDecoder(source: String): TitleBasics = {
    val components = source.split("\t")

    TitleBasics(
      tconst = components(0),
      titleType = components(1),
      primaryTitle = components(2),
      originalTitle = components(3),
      isAdult = toBoolean(components(4)),
      startYear = toInt(components(5)),
      endYear = toInt(components(6)),
      runtimeMinutes = toInt(components(7)),
      genres = components(8),
    )
  }

  def titlePrincipalsDecoder(source: String): TitlePrincipals = {
    val components = source.split("\t")

    TitlePrincipals(
      tconst = components(0),
      ordering = toInt(components(1)),
      nconst = components(2),
      category = components(3),
      job = components(4),
      characters = components(5),
    )
  }

  def titleRatingsDecoder(source: String): TitleRatings = {
    val components = source.split("\t")

    TitleRatings(
      tconst = components(0),
      averageRating = toDouble(components(1)),
      numVotes = toInt(components(2)),
    )
  }

  def nameBasicsDecoder(source: String): NameBasics = {
    val components = source.split("\t")

    NameBasics(
      nconst = components(0),
      primaryName = components(1),
      birthYear = toInt(components(2)),
      deathYear = toInt(components(3)),
      primaryProfession = components(4),
      knownForTitles = components(5),
    )
  }

  private def toBoolean(source: String) = Try(source.toInt == 1).getOrElse(false)

  private def toInt(source: String) = Try(source.toInt).getOrElse(0)

  private def toDouble(source: String) = Try(source.toDouble).getOrElse(0.0)
}
