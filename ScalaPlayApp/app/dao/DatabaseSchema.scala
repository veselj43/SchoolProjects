package dao

import scala.concurrent.{ ExecutionContext, Future }
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import models._

abstract class DatabaseSchema 
extends HasDatabaseConfigProvider[JdbcProfile] {

	protected class UsersTable(tag: Tag) extends Table[User](tag, "user") {
		def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
		def name = column[String]("name")
		def pass = column[String]("pass")

		def * = (id.?, name, pass) <> (User.tupled, User.unapply)
	}
	protected val users = TableQuery[UsersTable]

	protected class ItemsTable(tag: Tag) extends Table[Item](tag, "item") {
		def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
		def name = column[String]("name")

		def * = (id.?, name) <> (Item.tupled, Item.unapply)
	}
	protected val items = TableQuery[ItemsTable]

	protected class PathsTable(tag: Tag) extends Table[Path](tag, "path") {
		def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
		def name = column[String]("name")

		def * = (id.?, name) <> (Path.tupled, Path.unapply)
	}
	protected val paths = TableQuery[PathsTable]

	protected class DropLogsTable(tag: Tag) extends Table[DropLog](tag, "drop_log") {
		def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
		def kc = column[Int]("kill_count")
		def userID = column[Long]("user_id")
		def pathID = column[Long]("path_id")

		def user = foreignKey("fk_drop_log_user", userID, users)(_.id)
		def path = foreignKey("fk_drop_log_path1", pathID, paths)(_.id)

		def * = (id.?, kc, userID, pathID) <> (DropLog.tupled, DropLog.unapply)
	}
	protected val dropLogs = TableQuery[DropLogsTable]

	protected class DropLogHasItemsTable(tag: Tag) extends Table[DropLogHasItem](tag, "drop_log_has_item") {
		def dropLogID = column[Long]("drop_log_id")
		def itemID = column[Long]("item_id")

		def dropLog = foreignKey("fk_drop_log_has_item_drop_log1", dropLogID, dropLogs)(_.id)
		def item = foreignKey("fk_drop_log_has_item_item1", itemID, items)(_.id)

		def * = (dropLogID, itemID) <> (DropLogHasItem.tupled, DropLogHasItem.unapply)
	}
	protected val dropLogHasItems = TableQuery[DropLogHasItemsTable]
}