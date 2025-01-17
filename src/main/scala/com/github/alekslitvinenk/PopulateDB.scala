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

  println("Arguments:")

  for {
    arg <- args
  } println(arg)

  require(args.length == 4, "Not enough arguments")

  // We do care about memory footprint when importing our DB
  val threadPool = Executors.newFixedThreadPool(10)
  implicit val ecFixed = ExecutionContext.fromExecutor(threadPool)

  val db = Database.forConfig("imdb")

  val startTime = System.currentTimeMillis()

  case class SliceData(dropNum: Int, takeNum: Int)

  val chunkSize = 5000
  val batchSize = 100
  val sliceOpt: Option[SliceData] = None//Some(SliceData(0, 30000))

  val operations = List(
    fillTitleBasicsAndPrimaryTitleIndex(args(0)),
    fillTitlePrincipals(args(1)),
    fillTitleRatings(args(2)),
    fillNameBasicsAndPrimaryNameIndex(args(3))
  )

  // Get the future that completes when all the other futures complete
  val fAll = Future.reduceLeft(operations)((_, _) => ())

  Await.result(fAll, Duration.Inf)

  val timeSpent = System.currentTimeMillis() - startTime

  println(s"DB has been successfully populated with data in $timeSpent ms")

  db.close()
  threadPool.shutdown()

  def fillTitleBasicsAndPrimaryTitleIndex(filePath: String): Future[Unit] = {
    
    val tableName1 = "title_basics"
    val tableName2 = "title_index"

    def insertInBatches(chunk: List[String], batchSize: Int): Future[Unit] = {

      val batchInsertFutures = mutable.Queue[Future[Unit]]()

      val source = chunk
        .grouped(batchSize)

      while (source.hasNext) {
        val lines = source.next()

        val titleBasicsInsert = TitleBasicsTable ++= lines.map(titleBasicsDecoder)
        val titleIndexInsert = PrimaryTitleIndexTable ++=  lines.map(primaryTitleIndexDecoder)

        batchInsertFutures.enqueue(
          db.run(titleBasicsInsert)
            .recover { case e: Throwable =>
              println(s"Exception happened during batch insert for table $tableName1: ${e.getMessage}")
            }
            .map(_ => ())
        )
        
        batchInsertFutures.enqueue(
          db.run(titleIndexInsert)
            .recover { case e: Throwable =>
              println(s"Exception happened during batch insert for table $tableName2: ${e.getMessage}")
            }
            .map(_ => ()))
      }

      Future.reduceLeft(batchInsertFutures.toList)((_, _) => ())
    }

    for {
      _ <- db.run { TitleBasicsTable.schema.dropIfExists }
      _ <- db.run { TitleBasicsTable.schema.create }
      _ <- db.run { PrimaryTitleIndexTable.schema.dropIfExists }
      _ <- db.run { PrimaryTitleIndexTable.schema.create }
      _ <- fillData(filePath, chunkSize, batchSize, insertInBatches)
    } yield ()
  }

  def fillTitlePrincipals(filePath: String) =
    createAndPopulateTable(filePath, TitlePrincipalsTable, titlePrincipalsDecoder)

  def fillTitleRatings(filePath: String) =
    createAndPopulateTable(filePath, TitleRatingsTable, titleRatingsDecoder)

  def fillNameBasicsAndPrimaryNameIndex(filePath: String): Future[Unit] = {
    
    val tableName1 = "name_basics"
    val tableName2 = "name_index"

    def insertInBatches(chunk: List[String], batchSize: Int): Future[Unit] = {

      val batchInsertFutures = mutable.Queue[Future[Unit]]()

      val source = chunk
        .grouped(batchSize)

      while (source.hasNext) {
        val lines = source.next()

        val titleBasicsInsert = NameBasicsTable ++= lines.map(nameBasicsDecoder)
        val titleIndexInsert = PrimaryNameIndexTable ++=  lines.map(primaryNameIndexDecoder)

        batchInsertFutures.enqueue(
          db.run(titleBasicsInsert)
            .recover { case e: Throwable =>
              println(s"Exception happened during batch insert for table $tableName1: ${e.getMessage}")
            }
            .map(_ => ())
        )
        
        batchInsertFutures.enqueue(
          db.run(titleIndexInsert)
            .recover { case e: Throwable =>
              println(s"Exception happened during batch insert for table $tableName2: ${e.getMessage}")
            }
            .map(_ => ())
        )
      }

      Future.reduceLeft(batchInsertFutures.toList)((_, _) => ())
    }

    for {
      _ <- db.run { NameBasicsTable.schema.dropIfExists }
      _ <- db.run { NameBasicsTable.schema.create }
      _ <- db.run { PrimaryNameIndexTable.schema.dropIfExists }
      _ <- db.run { PrimaryNameIndexTable.schema.create }
      _ <- fillData(filePath, chunkSize, batchSize, insertInBatches)
    } yield ()
  }

  private def createAndPopulateTable[O, T <: Table[O]](filePath: String, table: TableQuery[T], converter: String => O): Future[Unit] = {

    def insertInBatches(chunk: List[String], batchSize: Int): Future[Unit] = {

      val batchInsertFutures = mutable.Queue[Future[Unit]]()

      val source = chunk
        .grouped(batchSize)

      while (source.hasNext) {
        val insertAction = table ++= source
          .next()
          .map(converter)

        batchInsertFutures.enqueue(
          db.run(insertAction)
            .recover { case e: Throwable =>
              println(s"Exception happened during batch insert for some table: ${e.getMessage}")
            }
            .map(_ => ())
        )
      }

      Future.reduceLeft(batchInsertFutures.toList)((_, _) => ())
    }

    for {
      _ <- db.run { table.schema.dropIfExists }
      _ <- db.run { table.schema.create }
      _ <- fillData(filePath, chunkSize, batchSize, insertInBatches)
    } yield ()
  }
  
  private def fillData(filePath: String, chunkSize: Int, batchSize: Int, func: (List[String], Int) => Future[Unit]): Future[Unit] = {
    val source = preprocessSource(Source.fromFile(filePath))
      .grouped(chunkSize)
  
    // We do care about HikariCP queue size, so let's wait till previous batch of futures completes
    while (source.hasNext) {
      Await.result(func(source.next(), batchSize), Duration.Inf)
    }
  
    Future.successful(())
  }

  private def preprocessSource(source: Source): Iterator[String] = {
    sliceOpt.fold {
      source
        .getLines
        // Skip column titles row
        .drop(1)
    } { sliceData =>
      source
        .getLines
        // Skip column titles row + given offset
        .slice(sliceData.dropNum + 1, sliceData.takeNum + 1)
    }
  }
}