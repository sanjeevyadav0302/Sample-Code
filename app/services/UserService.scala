package services

import javax.inject.Inject

import models.{AccessToken, RefreshToken, TokenCarrier}
import play.api.Configuration
import utility.JsonWebTokenUtility

import scala.concurrent.ExecutionContext


class UserService @Inject()(tokenService: TokenService,configuration: Configuration)(implicit ec: ExecutionContext) {

  private val logger = play.api.Logger(this.getClass)

  def getAuthorization(): TokenCarrier= {
    logger.info("Getting Authorization of the user")
        tokenService.generateAccessAndRefreshToken()
  }

  def generateAccessAndRefreshToken(accessToken: AccessToken,refreshToken: RefreshToken) : TokenCarrier ={
    logger.info("++++++Generate Access and refresh Service++++++")
    val genAccessToken  = JsonWebTokenUtility.createAccessToken(accessToken,configuration.get[String]("jwt.privateKey"))
    val genRefreshToken  = JsonWebTokenUtility.createRefreshTokenToken(refreshToken,configuration.get[String]("jwt.privateKey"))
    TokenCarrier(genAccessToken,genRefreshToken)
  }
}

