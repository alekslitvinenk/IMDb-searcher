package com.github.alekslitvinenk.db

import com.github.alekslitvinenk.domain.Protocol.JoitSearhResult
import slick.jdbc.MySQLProfile.api._

object Queries {

  def searchFilmByTitle(title: String) =
    sql"""
         |SELECT tb.primary_title AS title, tb.is_adult, tb.start_year, tb.end_year, tb.runtime_minutes, tb.genres, AVG(tr.average_rating) AS rating,
         |GROUP_CONCAT(
         |CONCAT(tp.category, '--', COALESCE(nb.primary_name,'\N'))
         |SEPARATOR ', ') as cast_and_crew
         |FROM title_basics AS tb
         |INNER JOIN title_principals AS tp ON tb.tconst = tp.tconst
         |LEFT JOIN name_basics AS nb ON nb.nconst = tp.nconst
         |LEFT JOIN title_ratings AS tr ON tb.tconst = tr.tconst
         |WHERE tb.primary_title = $title
         |GROUP BY tb.id
       """.stripMargin.as[JoitSearhResult]

  def searchTop10RatedFilmsByGenre(genre: String) = {
    sql"""
         |SELECT tb.primary_title AS title, tb.is_adult, tb.start_year, tb.end_year, tb.runtime_minutes, tb.genres, AVG(tr.average_rating) AS rating,
         |GROUP_CONCAT(
         |CONCAT(tp.category, '--', COALESCE(nb.primary_name,'\N'))
         |SEPARATOR ', ') as cast_and_crew
         |FROM title_basics AS tb
         |INNER JOIN title_principals AS tp ON tb.tconst = tp.tconst
         |LEFT JOIN name_basics AS nb ON nb.nconst = tp.nconst
         |LEFT JOIN title_ratings AS tr ON tb.tconst = tr.tconst
         |WHERE genres LIKE '%#$genre%'
         |GROUP BY tb.id
         |ORDER BY rating DESC
         |LIMIT 10
       """.stripMargin.as[JoitSearhResult]
  }
}
