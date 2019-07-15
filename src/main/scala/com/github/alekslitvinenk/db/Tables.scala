package com.github.alekslitvinenk.db

import com.github.alekslitvinenk.domain.Protocol._
import slick.jdbc.MySQLProfile.api._

object Tables {

  class TitleAkasTable(tag: Tag) extends Table[TitleAkas](tag, "title_akas") {

    def titleId = column[String]("title_id")
    def ordering = column[Int]("ordering")
    def title = column[String]("title")
    def region = column[String]("region")
    def language = column[String]("language")
    def types = column[String]("types")
    def attributes = column[String]("attributes")
    def isOriginalTitle = column[Boolean]("is_original_title")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    override def * = (
      titleId,
      ordering,
      title,
      region,
      language,
      types,
      attributes,
      isOriginalTitle,
      id
    ) <> (TitleAkas.tupled, TitleAkas.unapply)
  }

  lazy val TitleAkasTable = TableQuery[TitleAkasTable]

  class TitleBasicsTable(tag: Tag) extends Table[TitleBasics](tag, "title_basics") {

    def tconst = column[String]("tconst")
    def titleType = column[String]("title_type")
    def primaryTitle = column[String]("primary_title")
    def originalTitle = column[String]("original_title")
    def isAdult = column[Boolean]("is_adult")
    def startYear = column[Int]("start_year")
    def endYear = column[Int]("end_year")
    def runtimeMinutes = column[Int]("runtime_minutes")
    def genres = column[String]("genres")
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

  class TitleCrewTable(tag: Tag) extends Table[TitleCrew](tag, "title_crew") {

    def tconst = column[String]("tconst")
    def directors = column[String]("directors")
    def writers = column[String]("writers")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    override def * = (
      tconst,
      directors,
      writers,
      id
    ) <> (TitleCrew.tupled, TitleCrew.unapply)
  }

  lazy val TitleCrewTable = TableQuery[TitleCrewTable]

  class TitlePrincipalsTable(tag: Tag) extends Table[TitlePrincipals](tag, "title_principals") {

    def tconst = column[String]("tconst")
    def ordering = column[Int]("ordering")
    def nconst = column[String]("nconst")
    def category = column[String]("category")
    def job = column[String]("job")
    def characters = column[String]("characters")
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

    def tconst = column[String]("tconst")
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

    def nconst = column[String]("nconst")
    def primaryName = column[String]("primary_name")
    def birthYear = column[Int]("birth_year")
    def deathYear = column[Int]("death_year")
    def primaryProfession = column[String]("primary_profession")
    def knownForTitles = column[String]("known_for_titles")
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
