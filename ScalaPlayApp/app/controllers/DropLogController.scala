package controllers

import javax.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.ExecutionContext

import auth._
import dao.DropLogDAO
import models._

class DropLogController @Inject() (
    dropLogDao: DropLogDAO
)(implicit executionContext: ExecutionContext) extends Controller {

	val dropLogForm = Form(
		mapping(
			"log" -> mapping(
				"id" -> optional(longNumber),
				"kc" -> number,
				"user_id" -> longNumber,
				"path_id" -> longNumber
			)(DropLog.apply)(DropLog.unapply),
			"itemIDs" -> seq(longNumber)
		)(LogData.apply)(LogData.unapply)
	)

	def index = AuthAction.async { implicit request =>
		dropLogDao.getTotals(request.uid).map { x =>
			Ok(views.html.dropLog(
				dropLogDao.listFullData(request.uid), x, request.uid)
			)
		}
	}

	def addDropLog() = AuthAction.async { implicit request =>
		val newLog = dropLogForm.bindFromRequest.get

		dropLogDao.insert(newLog.log).flatMap { x =>
			dropLogDao.bindItems(x, newLog.itemIDs).map { c =>
				Redirect(routes.DropLogController.index)
			}
		}
	}

	def deleteDropLog(id: Option[Long]) = AuthAction.async { implicit request =>
		dropLogDao.delete(id).map { res =>
			Redirect(routes.DropLogController.index)
		}
	}

}
