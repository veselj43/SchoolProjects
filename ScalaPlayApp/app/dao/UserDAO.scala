package dao

import scala.concurrent.{ ExecutionContext, Future }
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.util.Try

import models.User

class UserDAO @Inject() 
(protected val dbConfigProvider: DatabaseConfigProvider)
(implicit executionContext: ExecutionContext) extends DatabaseSchema {

	private val data = users

	def all(): Future[Seq[User]] = db.run(data.result)

	def insert(record: User): Future[Long] = {
		db.run(data returning data.map(_.id) += record)
	}

	def delete(id: Option[Long]): Future[Try[Int]] = {
		db.run(data.filter(_.id === id).delete.asTry)
	}

	def auth(login: User): Future[Option[User]] = {
		db.run(data.filter(x => x.name === login.name && x.pass === login.pass).result).map {
			case x if x.length > 0 => { Some(x.head) }
			case x => { None }
		}
	}

}