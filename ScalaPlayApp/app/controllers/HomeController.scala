package controllers

import javax.inject._
import play.api.data.Form
import play.api.data.Forms._
import play.api._
import play.api.mvc._
import scala.concurrent.ExecutionContext

import auth._
import dao.UserDAO
import models.User

@Singleton
class HomeController @Inject() (
	userDAO: UserDAO
)(implicit executionContext: ExecutionContext) 
extends Controller {

	val loginForm = Form(
		mapping(
			"id" -> optional(longNumber),
			"name" -> nonEmptyText,
			"pass" -> nonEmptyText
		)(User.apply)(User.unapply)
	)

	def index = UserAction { userReq =>
		userReq.uid match {
			case Some(_) => Redirect(routes.DropLogController.index)
			case _ => Ok(views.html.index())
		}
	}

	def register = Action.async { implicit request =>
		val user: User = loginForm.bindFromRequest.get

		userDAO.insert(user).map { uid =>
			Redirect(routes.HomeController.index)
			.withSession(request.session + ("UID" -> uid.toString))
		}
	}

	def login = Action.async { implicit request =>
		val user: User = loginForm.bindFromRequest.get

		userDAO.auth(user).map { x =>
			x match {
				case Some(u) => {
					Redirect(routes.HomeController.index)
					.withSession(request.session + ("UID" -> u.id.get.toString))
				}
				case _ => Redirect(routes.HomeController.index)
			}
		}
	}

	def logout = Action {
		Redirect(routes.HomeController.index).withNewSession
	}

}
