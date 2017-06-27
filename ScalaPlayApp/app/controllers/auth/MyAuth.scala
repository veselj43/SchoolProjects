package auth

import scala.concurrent.{ ExecutionContext, Future }
import play.api._
import javax.inject._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.api.routing.Router

class UserRequest[A](val uid: Option[Long], request: Request[A]) extends WrappedRequest[A](request)

object UserAction extends ActionBuilder[UserRequest] with ActionTransformer[Request, UserRequest] {
	def transform[A](request: Request[A]) = Future.successful {
		new UserRequest(request.session.get("UID").map(_.toLong), request)
	}
}

class AuthRequest[A](val uid: Long, request: Request[A]) extends WrappedRequest[A](request)

object AuthAction extends ActionBuilder[AuthRequest] {
	def invokeBlock[A](request: Request[A], block: (AuthRequest[A]) => Future[Result]): Future[Result] = {
		request.session.get("UID")
		.map(uid => {
			block(new AuthRequest(uid.toLong, request))
		})
		.getOrElse(Future.successful(Results.Redirect("/")))
	}
}