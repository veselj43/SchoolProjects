package controllers

import javax.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.ExecutionContext

import auth._
import dao.ItemDAO
import models.Item

class ItemController @Inject() (
    itemDao: ItemDAO
)(implicit executionContext: ExecutionContext) extends Controller {

	val itemForm = Form(
		mapping(
			"id" -> optional(longNumber),
			"name" -> nonEmptyText
		)(Item.apply)(Item.unapply)
	)

	def index = AuthAction.async { implicit request =>
		itemDao.all().map { items =>
			Ok(views.html.admin.item(items))
		}
	}

	def addItem() = AuthAction.async { implicit request =>
	    val item: Item = itemForm.bindFromRequest.get
	    itemDao.insert(item).map(_ => Redirect(routes.ItemController.index))
	}

	def deleteItem(id: Option[Long]) = AuthAction.async { implicit request =>
		itemDao.delete(id).map { res =>
			Redirect(routes.ItemController.index)
		}
	}

}
