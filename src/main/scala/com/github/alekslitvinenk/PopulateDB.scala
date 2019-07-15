package com.github.alekslitvinenk

import com.github.alekslitvinenk.db.Tables._
import com.github.alekslitvinenk.domain.ProtocolFormat._
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.io.Source

object PopulateDB extends App {

  val db = Database.forConfig("imdb")

  val f1 = fillTitleAkas("title_akas.txt")
  val f2 = fillTitleBasics("title_basics.txt")
  val f3 = fillTitleCrew("title_crew.txt")
  val f4 = fillTitlePrincipals("title_principals.txt")
  val f5 = fillTitleRatings("title_ratings.txt")
  val f6 = fillNameBasics("name_basics.txt")

  val fAll = for {
    _ <- f1
    _ <- f2
    _ <- f3
    _ <- f4
    _ <- f5
    _ <- f6
  } yield ()

  Await.result(fAll, Duration.Inf)

  println("DB has been successfully populated with data!")

  def fillTitleAkas(filePath: String) = {

    val table = TitleAkasTable

    for {
      _ <- db.run { table.schema.dropIfExists }
      _ <- db.run { table.schema.create }
      _ <- {
        //val file = new File(filePath)
        val source = Source.fromResource(filePath)
        val insertsAction = table ++= source.getLines.map(titleAkasDecoder(_)).toSeq

        db.run(insertsAction)
      }
    } yield ()
  }

  def fillTitleBasics(filePath: String) = {

    val table = TitleBasicsTable

    for {
      _ <- db.run { table.schema.dropIfExists }
      _ <- db.run { table.schema.create }
      _ <- {
        //val file = new File(filePath)
        val source = Source.fromResource(filePath)
        val insertsAction = table ++= source.getLines.map(titleBasicsDecoder(_)).toSeq

        db.run(insertsAction)
      }
    } yield ()
  }

  def fillTitleCrew(filePath: String) = {

    val table = TitleCrewTable

    for {
      _ <- db.run { table.schema.dropIfExists }
      _ <- db.run { table.schema.create }
      _ <- {
        //val file = new File(filePath)
        val source = Source.fromResource(filePath)
        val insertsAction = table ++= source.getLines.map(titleCrewDecoder(_)).toSeq

        db.run(insertsAction)
      }
    } yield ()
  }

  def fillTitlePrincipals(filePath: String) = {

    val table = TitlePrincipalsTable

    for {
      _ <- db.run { table.schema.dropIfExists }
      _ <- db.run { table.schema.create }
      _ <- {
        //val file = new File(filePath)
        val source = Source.fromResource(filePath)
        val insertsAction = table ++= source.getLines.map(titlePrincipalsDecoder(_)).toSeq

        db.run(insertsAction)
      }
    } yield ()
  }

  def fillTitleRatings(filePath: String) = {

    val table = TitleRatingsTable

    for {
      _ <- db.run { table.schema.dropIfExists }
      _ <- db.run { table.schema.create }
      _ <- {
        //val file = new File(filePath)
        val source = Source.fromResource(filePath)
        val insertsAction = table ++= source.getLines.map(titleRatingsDecoder(_)).toSeq

        db.run(insertsAction)
      }
    } yield ()
  }

  def fillNameBasics(filePath: String) = {

    val table = NameBasicsTable

    for {
      _ <- db.run { table.schema.dropIfExists }
      _ <- db.run { table.schema.create }
      _ <- {
        //val file = new File(filePath)
        val source = Source.fromResource(filePath)
        val insertsAction = table ++= source.getLines.map(nameBasicsDecoder(_)).toSeq

        db.run(insertsAction)
      }
    } yield ()
  }
}
