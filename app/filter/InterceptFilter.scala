package filter

import javax.inject.Inject

import models.{AccessToken, RefreshToken}
import org.joda.time.DateTime
import play.api.Configuration
import play.api.libs.json.Json
import play.api.mvc._
import services.UserService
import utility.{TokenUtility, ValidateOauth}

import scala.concurrent.{ExecutionContext, Future}

class InterceptFilter @Inject()(parser: BodyParsers.Default,configuration: Configuration, userService: UserService)(implicit ex: ExecutionContext) extends ActionBuilderImpl(parser) {

  private val logger = play.api.Logger(this.getClass)
  val validateRequest = ValidateOauth

  def getSecretKey(key:String): String ={
    configuration.get[String](key)
  }

  def validateAccessToken(accessTokenJwt: String,refreshTokenJwt:String): (Boolean,AccessToken,RefreshToken,Boolean) = {
    logger.info("Access token : " + accessTokenJwt)
    logger.info("secret key is: " +getSecretKey("jwt.privateKey") )
    val  isValid  = TokenUtility.validateToken(accessTokenJwt, getSecretKey("jwt.privateKey"))
    logger.info("Access token valid ?" + isValid)

    val accessToken = validateRequest.parseAccessToken(accessTokenJwt).get;
    logger.info("Parsed access token is " + Json.toJson(accessToken))

    val isExpired = validateRequest.isExpired(accessToken.createdAt,accessToken.exp.toInt)
    logger.info("Access token is expired ? " + validateRequest.isExpired(accessToken.createdAt,accessToken.exp.toInt) + " Create Time " + accessToken.createdAt + " expiry in min "+accessToken.exp.toInt )

    val refreshToken = validateRequest.parseRefreshToken(refreshTokenJwt).get;
    var isAccessTokenUpdated = false;
    if(isExpired && validateRefreshToken(refreshTokenJwt)){
      logger.info("Access Token is getting refresh")
      isAccessTokenUpdated = true;
      ((isValid && (!isExpired || isAccessTokenUpdated)), accessToken.copy(createdAt = DateTime.now.toLocalDateTime.toString("yyyy-MM-dd'T'HH:mm:s")),refreshToken.copy(createdAt = DateTime.now.toLocalDateTime.toString("yyyy-MM-dd'T'HH:mm:s")),isAccessTokenUpdated)
    }else{
      ((isValid && (!isExpired || isAccessTokenUpdated)), accessToken,refreshToken,isAccessTokenUpdated)
    }
  }

  def validateRefreshToken(refreshTokenJwt: String): Boolean = {
    logger.info("Refresh token : " + refreshTokenJwt)
    logger.info("secret key is: " +getSecretKey("jwt.privateKey") )
    val  isValid  = TokenUtility.validateToken(refreshTokenJwt, getSecretKey("jwt.privateKey"))
    logger.info("Refresh token valid ? " + isValid)

    val refreshToken = validateRequest.parseRefreshToken(refreshTokenJwt).get;
    logger.info("Refresh token Parsed - " + Json.toJson(refreshToken))

    logger.info("Refresh token is expired ? " +  validateRequest.isExpired(refreshToken.createdAt,refreshToken.exp.toInt) +" Create Time " + refreshToken.createdAt + " expiry in min "+refreshToken.exp.toInt )

    isValid && !validateRequest.isExpired(refreshToken.createdAt,refreshToken.exp.toInt)
  }

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    val requestedPath: (String, String) = (request.host, request.uri)
    logger.info("===In Invoke block====Trying to access " + requestedPath._2)

    val validateRequest = ValidateOauth
    val accessToken = request.cookies.get("access_token")
    val refreshToken = request.cookies.get("refresh_token")

    (accessToken,refreshToken) match {
      case (Some(accessTokenJwt),Some(refreshTokenJwt)) => {
      val accessToken = validateRequest.parseAccessToken(accessTokenJwt.value).get;
        val tokenInfo = validateAccessToken(accessTokenJwt.value,refreshTokenJwt.value)
        tokenInfo._1 match {
          case true =>
            val user = validateRequest.parsePayload(accessToken.payload).get
            tokenInfo._4 match {
              case true =>
                val tokens = userService.generateAccessAndRefreshToken(tokenInfo._2, tokenInfo._3)
                logger.info("Serving request url by updating cookies " + tokens.accessToken);
                block(request).map(_.discardingCookies(DiscardingCookie("refresh_token"), DiscardingCookie("access_token")).withCookies(Cookie("access_token", tokens.accessToken), Cookie("refresh_token", tokens.refreshToken)))
              case false => logger.info("Serving request url"); block(request)
            }
          case false => logger.info("Access token Issue - Redirecting to login page")
            Future.successful(Results.Redirect("http://localhost:9000",302).discardingCookies(DiscardingCookie("refresh_token"),DiscardingCookie("access_token")))
        }
      }
      case _  => {
        logger.info("Access token Issue - Redirecting to login page")
        Future.successful(Results.Redirect(controllers.routes.UserController.loginBySocialSite()))
      }
    }
  }
}



