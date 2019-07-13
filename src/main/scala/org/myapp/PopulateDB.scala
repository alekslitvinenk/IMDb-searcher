package org.myapp

import java.io.File

import org.myapp.db.table.Tables._
import org.myapp.domain.ProtocolFormat.titleAkasDecoder
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.io.Source

object PopulateDB extends App {

  val db = Database.forConfig("imdb")

  Await.result(fillTitleAkas("/Users/alitvinenko/Downloads/title.akas.tsv"), Duration.Inf)

  def fillTitleAkas(filePath: String) = {

    for {
      _ <- db.run { TitleAkasTable.schema.create }
      _ <- {
        val file = new File(filePath)
        val source = Source.fromFile(file)
        val insertsAction = TitleAkasTable ++= source.getLines.map(titleAkasDecoder(_)).toSeq

        db.run(insertsAction)
      }
    } yield ()
  }
}
