package controllers

import scala.util.{Success, Failure}
import javax.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.ExecutionContext

import auth._
import dao.UserDAO
import models.User

class UserController @Inject() (
	userDao: UserDAO
)(implicit executionContext: ExecutionContext) extends Controller {

	val userForm = Form(
		mapping(
			"id" -> optional(longNumber),
			"name" -> nonEmptyText,
			"pass" -> nonEmptyText
		)(User.apply)(User.unapply)
	)

	def index = AuthAction.async { implicit request =>
		userDao.all().map { users =>
			Ok(views.html.admin.user(users))
		}
	}

	def addUser() = AuthAction.async { implicit request =>
		val user: User = userForm.bindFromRequest.get
		userDao.insert(user).map(_ => Redirect(routes.UserController.index))
	}

	def deleteUser(id: Option[Long]) = AuthAction.async { implicit request =>
		userDao.delete(id).map { res =>
			res match {
				case Success(r) => Redirect(routes.UserController.index).flashing("success" -> "Deleted.")
				case Failure(e) => Redirect(routes.UserController.index).flashing("error" -> "User cannot be deleted.")
			}
		}
	}

}