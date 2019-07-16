package com.github.alekslitvinenk.db

import com.github.alekslitvinenk.domain.Protocol._
import slick.jdbc.MySQLProfile.api._

object Tables {

  class TitleBasicsTable(tag: Tag) extends Table[TitleBasics](tag, "title_basics") {

    def tconst = column[String]("tconst", O.Length(30))
    def titleType = column[String]("title_type", O.Length(30))
    def primaryTitle = column[String]("primary_title", O.Length(100))
    def originalTitle = column[String]("original_title", O.Length(100))
    def isAdult = column[Boolean]("is_adult")
    def startYear = column[Int]("start_year")
    def endYear = column[Int]("end_year")
    def runtimeMinutes = column[Int]("runtime_minutes")
    def genres = column[String]("genres", O.Length(100))
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

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
      id,
    ) <> (TitleBasics.tupled, TitleBasics.unapply)
  }

  lazy val TitleBasicsTable = TableQuery[TitleBasicsTable]

  class TitlePrincipalsTable(tag: Tag) extends Table[TitlePrincipals](tag, "title_principals") {

    def tconst = column[String]("tconst", O.Length(30))
    def ordering = column[Int]("ordering")
    def nconst = column[String]("nconst", O.Length(30))
    def category = column[String]("category", O.Length(100))
    def job = column[String]("job", O.Length(100))
    def characters = column[String]("characters", O.Length(100))
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    override def * = (
      tconst,
      ordering,
      nconst,
      category,
      job,
      characters,
      id,
    ) <> (TitlePrincipals.tupled, TitlePrincipals.unapply)
  }

  lazy val TitlePrincipalsTable = TableQuery[TitlePrincipalsTable]

  class TitleRatingsTable(tag: Tag) extends Table[TitleRatings](tag, "title_ratings") {

    def tconst = column[String]("tconst", O.Length(30))
    def averageRating = column[Double]("average_rating")
    def numVotes = column[Int]("num_votes")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    override def * = (
      tconst,
      averageRating,
      numVotes,
      id,
    ) <> (TitleRatings.tupled, TitleRatings.unapply)
  }

  lazy val TitleRatingsTable = TableQuery[TitleRatingsTable]

  class NameBasicsTable(tag: Tag) extends Table[NameBasics](tag, "name_basics") {

    def nconst = column[String]("nconst", O.Length(30))
    def primaryName = column[String]("primary_name", O.Length(100))
    def birthYear = column[Int]("birth_year")
    def deathYear = column[Int]("death_year")
    def primaryProfession = column[String]("primary_profession", O.Length(100))
    def knownForTitles = column[String]("known_for_titles", O.Length(100))
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    override def * = (
      nconst,
      primaryName,
      birthYear,
      deathYear,
      primaryProfession,
      knownForTitles,
      id,
    ) <> (NameBasics.tupled, NameBasics.unapply)
  }

  lazy val NameBasicsTable = TableQuery[NameBasicsTable]

}
