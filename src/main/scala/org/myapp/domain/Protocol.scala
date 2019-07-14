package org.myapp.domain

object Protocol {

  case class TitleAkas(
    titleId: String,
    ordering: Int,
    title: String,
    region: String,
    language: String,
    types: String,
    attributes: String,
    isOriginalTitle: Boolean,
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

  case class TitleCrew(
    tconst: String,
    directors: String,
    writers: String,
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

}
