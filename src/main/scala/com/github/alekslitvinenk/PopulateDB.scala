package com.github.alekslitvinenk

import com.github.alekslitvinenk.db.Tables._
import com.github.alekslitvinenk.domain.ProtocolFormat._
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.io.Source

object PopulateDB extends App {

  require(args.length == 4, "Not enough arguments")

  val db = Database.forConfig("imdb")

  val operations = List(
    fillTitleBasics(args(0)),
    fillTitlePrincipals(args(1)),
    fillTitleRatings(args(2)),
    fillNameBasics(args(3))
  )

  // Get the future that completes when all the other futures complete
  val fAll = Future.reduceLeft(operations)((_, _) => ())

  Await.result(fAll, Duration.Inf)

  println("DB has been successfully populated with data!")

  def fillTitleBasics(filePath: String): Future[Unit] =
    createAndPopulateTable(filePath, TitleBasicsTable, titleBasicsDecoder)

  def fillTitlePrincipals(filePath: String) =
    createAndPopulateTable(filePath, TitlePrincipalsTable, titlePrincipalsDecoder)

  def fillTitleRatings(filePath: String) =
    createAndPopulateTable(filePath, TitleRatingsTable, titleRatingsDecoder)

  def fillNameBasics(filePath: String) =
    createAndPopulateTable(filePath, NameBasicsTable, nameBasicsDecoder)

  // TODO: Insert data into DB by chunks
  private def createAndPopulateTable[O, T <: Table[O]](filePath: String, table: TableQuery[T], converter: String => O) = {
    for {
      _ <- db.run { table.schema.dropIfExists }
      _ <- db.run { table.schema.create }
      _ <- {
        val source = Source.fromFile(filePath)
        val insertsAction = table ++= source
          .getLines
          // Skip columns titles row
          .toStream.tail.take(3000)
          .map(converter(_))

        db.run(insertsAction)
      }
    } yield ()
  }
}