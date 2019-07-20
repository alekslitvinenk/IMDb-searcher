package com.github.alekslitvinenk.db

import com.github.alekslitvinenk.domain.Protocol._
import slick.jdbc.MySQLProfile.api._

object Tables {

  class PrimaryTitleIndexTable(tag: Tag) extends Table[PrimaryTitleIndex](tag, "primary_title_index") {

    def tconst = column[Long]("tconst", O.PrimaryKey)
    def thash = column[Long]("thash")

    override def * = (
      tconst,
      thash
    ) <> (PrimaryTitleIndex.tupled, PrimaryTitleIndex.unapply)

    def idx = index("idx_thash", thash)
  }

  lazy val PrimaryTitleIndexTable = TableQuery[PrimaryTitleIndexTable]

  class TitleBasicsTable(tag: Tag) extends Table[TitleBasics](tag, "title_basics") {

    def tconst = column[Long]("tconst", O.PrimaryKey)
    def titleType = column[String]("title_type", O.Length(30))
    def primaryTitle = column[String]("primary_title")
    def originalTitle = column[String]("original_title")
    def isAdult = column[Boolean]("is_adult")
    def startYear = column[Int]("start_year")
    def endYear = column[Int]("end_year")
    def runtimeMinutes = column[Int]("runtime_minutes")
    def genres = column[String]("genres", O.Length(100))

    override def * = (
      tconst,
      titleType,
      primaryTitle,
      originalTitle,
      isAdult,
      startYear,
      endYear,
      runtimeMinutes,
      genres,
    ) <> (TitleBasics.tupled, TitleBasics.unapply)
  }

  lazy val TitleBasicsTable = TableQuery[TitleBasicsTable]

  class TitlePrincipalsTable(tag: Tag) extends Table[TitlePrincipals](tag, "title_principals") {

    def tconst = column[Long]("tconst")
    def ordering = column[Int]("ordering")
    def nconst = column[Long]("nconst")
    def category = column[String]("category")
    def job = column[String]("job")
    def characters = column[String]("characters")

    override def * = (
      tconst,
      ordering,
      nconst,
      category,
      job,
      characters,
    ) <> (TitlePrincipals.tupled, TitlePrincipals.unapply)

    def idx = index("idx_signum", (tconst, nconst))
  }

  lazy val TitlePrincipalsTable = TableQuery[TitlePrincipalsTable]

  class TitleRatingsTable(tag: Tag) extends Table[TitleRatings](tag, "title_ratings") {

    def tconst = column[Long]("tconst", O.PrimaryKey)
    def averageRating = column[Double]("average_rating")
    def numVotes = column[Int]("num_votes")

    override def * = (
      tconst,
      averageRating,
      numVotes,
    ) <> (TitleRatings.tupled, TitleRatings.unapply)

    def idx = index("idx_rating", averageRating)
  }

  lazy val TitleRatingsTable = TableQuery[TitleRatingsTable]

  class NameBasicsTable(tag: Tag) extends Table[NameBasics](tag, "name_basics") {

    def nconst = column[Long]("nconst", O.PrimaryKey)
    def primaryName = column[String]("primary_name", O.Length(150))
    def birthYear = column[Int]("birth_year")
    def deathYear = column[Int]("death_year")
    def primaryProfession = column[String]("primary_profession", O.Length(150))
    def knownForTitles = column[String]("known_for_titles")

    override def * = (
      nconst,
      primaryName,
      birthYear,
      deathYear,
      primaryProfession,
      knownForTitles,
    ) <> (NameBasics.tupled, NameBasics.unapply)
  }

  lazy val NameBasicsTable = TableQuery[NameBasicsTable]

}
