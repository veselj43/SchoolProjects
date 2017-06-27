package dao

import scala.concurrent.{ ExecutionContext, Future }
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import models.Item

class ItemDAO @Inject() 
(protected val dbConfigProvider: DatabaseConfigProvider)
(implicit executionContext: ExecutionContext) extends DatabaseSchema {

	private val data = items

	def all(): Future[Seq[Item]] = db.run(data.result)

	def insert(record: Item): Future[Unit] = {
		db.run(data += record).map { _ => () }
	}

	def delete(id: Option[Long]): Future[Int] = {
		db.run(data.filter(_.id === id).delete)
	}

}