package com.github.alekslitvinenk.domain

import slick.jdbc.GetResult

object Protocol {

  case class PrimaryTitleIndex (
    primaryTitle: String,
    tconst: String,
    id: Long = 0L,
  )

  case class TitleBasics(
    tconst: String,
    titleType: String,
    primaryTitle: String,
    originalTitle: String,
    isAdult: Boolean,
    startYear: Int,
    endYear: Int,
    runtimeMinutes: Int,
    genres: String,
    id: Long = 0L,
  )

  case class TitlePrincipals(
    tconst: String,
    ordering: Int,
    nconst: String,
    category: String,
    job: String,
    characters: String,
    id: Long = 0L,
  )

  case class TitleRatings(
    tconst: String,
    averageRating: Double,
    numVotes: Int,
    id: Long = 0L,
  )

  case class NameBasics(
    nconst: String,
    primaryName: String,
    birthYear: Int,
    deathYear: Int,
    primaryProfession: String,
    knownForTitles: String,
    id: Long = 0L,
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
