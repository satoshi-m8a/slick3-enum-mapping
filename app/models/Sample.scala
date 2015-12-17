package models

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import models.Color.Color

import scala.concurrent.ExecutionContext

object Color extends Enumeration {
  type Color = Value
  val Blue = Value("Blue")
  val Red = Value("Red")
  val Green = Value("Green")
}

case class Sample(name:String, id:Int, c:Color)

// Schemas
@Singleton
class ColorDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import driver.api._

  class SampleTable(tag: Tag) extends Table[Sample](tag, "Sample") {
    def name  = column[String]("NAME")
    def id    = column[Int]("ID")
    def color = column[Color]("COLOR")
    def * = (name, id, color) <> (Sample.tupled, Sample.unapply)
  }

  implicit val colorMapper  = MappedColumnType.base[Color, String](
    e => e.toString,
    s => Color.withName(s)
  )
}