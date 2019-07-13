package org.myapp.db.table

import org.myapp.domain.Protocol.TitleAkas
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

}
