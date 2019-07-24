package com.github.alekslitvinenk.common

import com.github.alekslitvinenk.domain.Protocol.{NameBasics, PrimaryNameIndex, PrimaryTitleIndex, TitleBasics, TitlePrincipals, TitleRatings}

object TestData {

  def stringToTitleBasics: (String, TitleBasics) = {

    val stringData = "tt0000001\tshort\tCarmencita\tCarmencita\t0\t1894\t\\N\t1\tDocumentary,Short"

    val titleBasics = TitleBasics(
      tconst = 1L,
      titleType = "short",
      primaryTitle = "Carmencita",
      originalTitle = "Carmencita",
      isAdult = false,
      startYear = 1894,
      endYear = 0,
      runtimeMinutes = 1,
      genres = "Documentary,Short"
    )

    (stringData, titleBasics)
  }

  def stringToPrimaryTitleIndex: (String, PrimaryTitleIndex) = {

    val stringData = "tt0000001\tshort\tCarmencita\tCarmencita\t0\t1894\t\\N\t1\tDocumentary,Short"

    val titleBasics = PrimaryTitleIndex(
      1L,
      -1744172555L
    )

    (stringData, titleBasics)
  }

  def stringToTitlePrincipals: (String, TitlePrincipals) = {
    val stringData = "tt0000001\t1\tnm1588970\tself\t\\N\t[\"Herself\"]"

    val titlePrincipals = TitlePrincipals(
      tconst = 1L,
      ordering = 1,
      nconst = 1588970L,
      category = "self",
      job = "\\N",
      characters = "[\"Herself\"]"
    )

    (stringData, titlePrincipals)
  }

  def stringToTitleRatings: (String, TitleRatings) = {
    val stringData = "tt0000001\t5.8\t1521"

    val titleRatings = TitleRatings(
      tconst = 1L,
      averageRating = 5.8,
      numVotes = 1521
    )

    (stringData, titleRatings)
  }

  def stringToNameBasics: (String, NameBasics) = {
    val stringData = "nm0000001\tFred Astaire\t1899\t1987\tsoundtrack,actor,miscellaneous\ttt0072308,tt0043044,tt0053137,tt0050419"

    val nameBasics = NameBasics(
      nconst = 1L,
      primaryName = "Fred Astaire",
      birthYear = 1899,
      deathYear = 1987,
      primaryProfession = "soundtrack,actor,miscellaneous",
      knownForTitles = "72308,43044,53137,50419",
    )

    (stringData, nameBasics)
  }

  def stringToPrimaryNameIndex: (String, PrimaryNameIndex) = {
    val stringData = "nm0000001\tFred Astaire\t1899\t1987\tsoundtrack,actor,miscellaneous\ttt0072308,tt0043044,tt0053137,tt0050419"

    val primaryNameIndex = PrimaryNameIndex(
      nconst = 1L,
      nhash = -376955608L
    )

    (stringData, primaryNameIndex)
  }
}
