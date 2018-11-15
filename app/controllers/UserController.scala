package controllers

import javax.inject.Inject

import factory.AuthenticationFactor
import filter.InterceptFilter
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.UserService
import utility.Constants

import scala.concurrent.{ExecutionContext, Future}


class UserController @Inject()(cc: ControllerComponents, userService: UserService, interceptFilter: InterceptFilter )(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport {

  def index = interceptFilter {
    Ok
  }


  def login = Action.async {
    implicit request =>
      val requestState = request.queryString("state")
      val sessionState = request.session.get("state")
      val response = requestState match {
        case value if (!requestState.isEmpty || !sessionState.isEmpty && requestState.equals(sessionState)) => {
          userService.getAuthorization()
        }
      }
      Future.successful(Ok(views.html.sucess()).discardingCookies(DiscardingCookie("access_token"),DiscardingCookie("refresh_token")).withCookies(Cookie("access_token", response.accessToken),Cookie("refresh_token", response.refreshToken)))
  }

  def home = interceptFilter {
    Ok(views.html.home())
  }

  def loginBySocialSite() =  Action{
    implicit request => Ok(views.html.login( AuthenticationFactor.getAllLoginLinks()))
  }

  def doLogin(url:String) = Action{
    request =>
      val x = Constants.URLS.filter(p => p._1 == url).head
      Results.Redirect(x._2).withSession("state" -> x._3)
  }


  def removeCookie = Action{
    Ok.discardingCookies(DiscardingCookie("access_token"),DiscardingCookie("refresh_token")).withCookies(Cookie("access_token","updatedToken"),Cookie("refresh_token","refreshToken"))
  }
}
