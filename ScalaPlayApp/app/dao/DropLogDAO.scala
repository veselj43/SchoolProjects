package dao

import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.concurrent.duration.Duration
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import models._

class DropLogDAO @Inject() 
(protected val dbConfigProvider: DatabaseConfigProvider)
(implicit executionContext: ExecutionContext) extends DatabaseSchema {

	private val data = dropLogs

	def all(): Future[Seq[DropLog]] = {
		db.run(data.result)
	}

	def insert(record: DropLog) = {
		db.run(data returning data.map(_.id) += record)
	}

	def delete(id: Option[Long]): Future[Int] = {
		db.run(dropLogHasItems.filter(_.dropLogID === id).delete)
		db.run(data.filter(_.id === id).delete)
	}

	def bindItems(id: Long, itemIDs: Seq[Long]): Future[Unit] = {
		db.run(dropLogHasItems ++= itemIDs.filter(_ != 0).map{iID => DropLogHasItem(id, iID)}).map { _ => () }
	}

	def listItems(dropLogID: Long): Seq[Item] = {
		val query = for {
			x <- dropLogHasItems.filter(_.dropLogID === dropLogID)
			i <- x.item
		} yield i

		Await.result(db.run(query.result), Duration.Inf)
	}

	def listFullData(uid: Long = -1L): Seq[DropLogData] = {

		val baseData = uid match {
			case -1L => data
			case id => data.filter(_.userID === id)
		}

		val query = for {
			(log, hasItem) <- 
				(baseData join paths on (_.pathID === _.id)) joinLeft 
				(dropLogHasItems join items on (_.itemID === _.id)) on (_._1.id === _._1.dropLogID)
		} yield (log, hasItem.map(_._2))

		Await.result(
			db.run(query.result.map { x =>
				x.groupBy(_._1).map {
					case (l,i) => (l,i.map(_._2))
				}.toSeq
			}).map(_.map(x => 
				DropLogData(x._1._1, x._1._2, x._2))
			), 
			Duration.Inf
		).sortWith(_.log.id.get > _.log.id.get)

	}

	def getDryStats(uid: Long) = {
		val baseData = data.filter(_.userID === uid)

		def getLast(iid: Long): Long = {
			Await.result(
				db.run(
					(baseData join dropLogHasItems.filter(_.itemID === iid) on (_.id === _.dropLogID))
					.sortBy(_._1.id.desc).map(_._1.id).result
				).map(_.headOption.getOrElse(Long.MaxValue)), 
			Duration.Inf)
		}

		def getDry(id: Long) = {
			db.run(baseData.filter(_.id > id).map(_.kc).sum.result).map(_.getOrElse(-1))
		}

		for {
			fang <- getDry(getLast(1L))
			web <- getDry(getLast(2L))
			eye <- getDry(getLast(3L))
			leg <- getDry(getLast(4L))
		} yield (
			List(leg, eye, web, fang).min, 
			leg, 
			eye, 
			web, 
			fang, 
			List(eye, web, fang).min
		)
	}

	def getCounts(uid: Long = -1L) = {
		val baseData = uid match {
			case -1L => data
			case id => data.filter(_.userID === id)
		}

		val query1 = baseData.map(_.kc).sum
		val query2 = (baseData join dropLogHasItems on (_.id === _.dropLogID))

		val query21 = query2.filter(_._2.itemID === 1L).length
		val query22 = query2.filter(_._2.itemID === 2L).length
		val query23 = query2.filter(_._2.itemID === 3L).length
		val query24 = query2.filter(_._2.itemID === 4L).length

		for {
			kc <- db.run(query1.result).map(_.getOrElse(0))
			fangs <- db.run(query21.result)
			webs <- db.run(query22.result)
			eyes <- db.run(query23.result)
			legs <- db.run(query24.result)
			dryStats <- getDryStats(uid)
		} yield (kc, legs, eyes, webs, fangs, (eyes + webs + fangs))
	}

	def getTotals(uid: Long) = {
		for {
			counts <- getCounts(uid)
			dryStats <- getDryStats(uid)
		} yield (counts, dryStats)
	}

}