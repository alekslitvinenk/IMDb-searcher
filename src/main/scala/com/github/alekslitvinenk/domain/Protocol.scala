package com.github.alekslitvinenk.domain

import slick.jdbc.GetResult

object Protocol {

  case class PrimaryTitleIndex (
    tconst: Long,
    thash: Long,
  )

  case class TitleBasics(
    tconst: Long,
    titleType: String,
    primaryTitle: String,
    originalTitle: String,
    isAdult: Boolean,
    startYear: Int,
    endYear: Int,
    runtimeMinutes: Int,
    genres: String,
  )

  case class TitlePrincipals(
    tconst: Long,
    ordering: Int,
    nconst: Long,
    category: String,
    job: String,
    characters: String,
  )

  case class TitleRatings(
    tconst: Long,
    averageRating: Double,
    numVotes: Int,
  )

  case class NameBasics(
    nconst: Long,
    primaryName: String,
    birthYear: Int,
    deathYear: Int,
    primaryProfession: String,
    knownForTitles: String,
  )

  implicit val getMyMatch = GetResult(r => JoitSearhResult(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))

  case class JoitSearhResult(
    title: String,
    isAdult: Boolean,
    startYear: Int,
    endYear: Int,
    runTime: Int,
    genres: String,
    rating: Double,
    castAndCrew: String
  )

  case class SearchResult(
    items: Vector[JoitSearhResult]
  )

}
