package com.github.alekslitvinenk

import java.util.concurrent.Executors

import com.github.alekslitvinenk.db.Tables._
import com.github.alekslitvinenk.domain.ProtocolFormat._
import slick.jdbc.MySQLProfile.api._

import scala.collection.mutable
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.Source

object PopulateDB extends App {

  require(args.length == 4, "Not enough arguments")

  // We do care about memory footprint when importing our DB
  val threadPool = Executors.newFixedThreadPool(10)
  implicit val ecFixed = ExecutionContext.fromExecutor(threadPool)

  val db = Database.forConfig("imdb")

  val startTime = System.currentTimeMillis()

  val operations = List(
    //fillTitleBasicsAndTPrimaryTitleIndex(args(0)),
    fillTitlePrincipals(args(1)),
    //fillTitleRatings(args(2)),
    //fillNameBasics(args(3))
  )

  // Get the future that completes when all the other futures complete
  val fAll = Future.reduceLeft(operations)((_, _) => ())

  Await.result(fAll, Duration.Inf)

  val timeSpent = System.currentTimeMillis() - startTime

  println(s"DB has been successfully populated with data in $timeSpent ms")

  db.close()
  threadPool.shutdown()

  def fillTitleBasicsAndTPrimaryTitleIndex(filePath: String): Future[Unit] = {

    val chunkSize = 10000
    val batchSize = 100

    def insertInBatches(chunk: List[String], batchSize: Int) = {

      val sideFutures = mutable.Queue[Future[Unit]]()

      val source = chunk
        .grouped(batchSize)

      while (source.hasNext) {
        val lines = source.next()

        val titleBasicsInsert = TitleBasicsTable ++= lines.map(titleBasicsDecoder(_))
        val titleIndexInser = PrimaryTitleIndexTable ++=  lines.map(primaryTitleIndexDecoder(_))

        sideFutures.enqueue(db.run(titleBasicsInsert).map(_ => ()))
        sideFutures.enqueue(db.run(titleIndexInser).map(_ => ()))
      }

      Future.reduceLeft(sideFutures.toList)((_, _) => ())
    }

    for {
      //_ <- db.run { TitleBasicsTable.schema.dropIfExists }
      //_ <- db.run { TitleBasicsTable.schema.create }
      //_ <- db.run { PrimaryTitleIndexTable.schema.dropIfExists }
      //_ <- db.run { PrimaryTitleIndexTable.schema.create }
      _ <- {

        val source = Source.fromFile(filePath)
          .getLines
          // Skip column titles row
          .drop(600001)
          //.take(400000)
          .grouped(chunkSize)

        // We do care about HikariCP queue size, so let's wait till previous batch of futures completes
        while (source.hasNext) {
          Await.result(insertInBatches(source.next(), batchSize), Duration.Inf)
        }

        Future.successful(())
      }
    } yield ()
  }

  def fillTitlePrincipals(filePath: String) =
    createAndPopulateTable(filePath, TitlePrincipalsTable, titlePrincipalsDecoder)

  def fillTitleRatings(filePath: String) =
    createAndPopulateTable(filePath, TitleRatingsTable, titleRatingsDecoder)

  def fillNameBasics(filePath: String) =
    createAndPopulateTable(filePath, NameBasicsTable, nameBasicsDecoder)

  private def createAndPopulateTable[O, T <: Table[O]](filePath: String, table: TableQuery[T], converter: String => O) = {

    val chunkSize = 5000
    val batchSize = 100

    def insertInBatches(chunk: List[String], batchSize: Int) = {

      val sideFutures = mutable.Queue[Future[Unit]]()

      val source = chunk
        .grouped(batchSize)

      while (source.hasNext) {
        val insertAction = table ++= source
          .next()
          .map(converter(_))

        sideFutures.enqueue(db.run(insertAction).map(_ => ()))
      }

      Future.reduceLeft(sideFutures.toList)((_, _) => ())
    }

    for {
      _ <- db.run { table.schema.dropIfExists }
      _ <- db.run { table.schema.create }
      _ <- {

        val source = Source.fromFile(filePath)
          .getLines
          // Skip column titles row
          .drop(1)
          //.take(1000000)
          .grouped(chunkSize)

        // We do care about HikariCP queue size, so let's wait till previous batch of futures completes
        while (source.hasNext) {
          Await.result(insertInBatches(source.next(), batchSize), Duration.Inf)
        }

        Future.successful(())
      }
    } yield ()
  }
}