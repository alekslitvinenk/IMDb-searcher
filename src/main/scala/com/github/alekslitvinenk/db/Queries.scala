package com.github.alekslitvinenk.db

import com.github.alekslitvinenk.domain.Protocol.JoitSearhResult
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class Queries(dataBase: Database)(implicit executionContext: ExecutionContext) {

  def searchFilmByTitle(title: String) = {

    val titleHash = title.hashCode

    val query = sql"""
         |SELECT tb.primary_title AS title, tb.is_adult, tb.start_year, tb.end_year, tb.runtime_minutes, tb.genres, AVG(tr.average_rating) AS rating,
         |GROUP_CONCAT(
         |CONCAT(tp.category, '--', COALESCE(nb.primary_name,'\N'))
         |SEPARATOR ', ') as cast_and_crew
         |FROM primary_title_index AS pti
         |INNER JOIN title_basics AS tb ON pti.tconst = tb.tconst
         |INNER JOIN title_principals AS tp ON tb.tconst = tp.tconst
         |LEFT JOIN name_basics AS nb ON nb.nconst = tp.nconst
         |LEFT JOIN title_ratings AS tr ON tb.tconst = tr.tconst
         |WHERE pti.thash=$titleHash
         |GROUP BY tb.tconst
       """.stripMargin.as[JoitSearhResult]

      dataBase.run(query)
    }

  def searchTop10RatedFilmsByGenre(genre: String) = {
    def loop(genre: String, pageNum: Int, pageSize: Int): Future[Vector[JoitSearhResult]] = {
      searchTop10RatedFilmsByGenreInternal(genre, pageNum, pageSize).flatMap { f =>
        if (f.isEmpty)
          loop(genre, pageNum + 1, pageSize)
        else
          Future.successful(f)
      }
    }

    loop(genre, 0, 10000)
  }

  def searchKevinBaconDegrees(person: String) = {
    val KevinBacon = 102L

    def loop(persons: Vector[Int], circleNum: Int): Future[Int] = {
      println("persons: " + persons.length + " : " + persons.take(10).mkString(",") + "..." + persons.takeRight(10).mkString(","))
      if(persons.nonEmpty && circleNum < 6) {
        getClosestCircleByIds(persons).flatMap { rez =>
          println("circleNum: " + circleNum)

          if (rez.contains(KevinBacon)) Future.successful(circleNum)
          else loop(rez, circleNum + 1)
        }
      } else Future.successful(0)
    }

    getPersonIdByName(person).flatMap { id =>
      loop(id, 1)
    }
  }

  private def getClosestCircleByIds(persons: Vector[Int]) = {
    val personsStr = persons.mkString(" ")

    val query =
      sql"""
        |SELECT DISTINCT nconst FROM title_principals
        |WHERE tconst IN
        |(SELECT DISTINCT tconst
        |FROM
        |title_principals
        |WHERE nconst IN ($personsStr))
      """.stripMargin.as[Int]

    dataBase.run(query)
  }

  private def getPersonIdByName(personName: String) = {
    val nameHash = personName.hashCode

    val query =
      sql"""
        |SELECT nconst
        |FROM
        |primary_name_index
        |WHERE nhash=$nameHash
        |LIMIT 1
      """.stripMargin.as[Int]

    dataBase.run(query)
  }

  private def searchTop10RatedFilmsByGenreInternal(genre: String, pageNum: Int, pageSize: Int) = {

    val offset = pageNum * pageSize

    val query = sql"""
         |SELECT tb.primary_title AS title, tb.is_adult, tb.start_year, tb.end_year, tb.runtime_minutes, tb.genres, AVG(top.average_rating) AS rating,
         |GROUP_CONCAT(
         |CONCAT(tp.category, '--', COALESCE(nb.primary_name,'\N'))
         |SEPARATOR ', ') as cast_and_crew
         |FROM
         |(SELECT * FROM title_ratings AS tr
         |ORDER BY average_rating DESC
         |LIMIT $offset, $pageSize) AS top
         |INNER JOIN title_basics AS tb ON top.tconst=tb.tconst
         |INNER JOIN title_principals AS tp ON tb.tconst = tp.tconst
         |LEFT JOIN name_basics AS nb ON nb.nconst = tp.nconst
         |WHERE tb.genres LIKE '%#$genre%'
         |GROUP BY top.tconst
         |ORDER BY top.average_rating DESC
         |LIMIT 10
       """.stripMargin.as[JoitSearhResult]

    dataBase.run(query)
  }
}

object Queries {
  def apply(database: Database)(implicit executionContext: ExecutionContext): Queries = new Queries(database)
}
