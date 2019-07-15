package com.github.alekslitvinenk.db

import slick.jdbc.GetResult
import slick.jdbc.MySQLProfile.api._

object Queries {

  implicit val getMyMatch = GetResult(r => JointTitleData(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))

  case class JointTitleData(
    title: String,
    region: String,
    language: String,
    primaryTitle: String,
    originalTitle: String,
    startYear: Int,
    endYear: Int,
    genres: String,
    primaryName: String,
    primaryProfession: String,
  )

  def searchByTitle(title: String) =
    sql"""
         |SELECT title, region, language, primary_title, original_title, start_year, end_year, genres, primary_name, primary_profession
         |FROM title_akas AS ta
         |INNER JOIN title_basics AS tb ON ta.title_id = tb.tconst
         |INNER JOIN title_principals AS tp ON tb.tconst = tp.tconst
         |INNER JOIN name_basics AS nb ON nb.nconst = tp.nconst
         |WHERE ta.title = $title
       """.stripMargin.as[JointTitleData]
}
