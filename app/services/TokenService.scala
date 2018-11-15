package services

import javax.inject.Inject

import models._
import org.joda.time.DateTime
import play.api.Configuration
import utility.{JsonWebTokenUtility, TokenGenerator}

import scala.concurrent.ExecutionContext


class TokenService @Inject()(utility:TokenGenerator, config: Configuration)(implicit ex:ExecutionContext)  {

  private val logger = play.api.Logger(this.getClass)


  def generateAccessAndRefreshToken() : TokenCarrier = {
    logger.info("++++++Generate Access and refresh Service++++++")
    val payload =
      """{"email":"sabarish@gmail.com","firstName": "Sabarish_Update","lastName":"PV", "user_groups":["id1", "id2"],"roles":["5a7a6c2377c881647d3c95cd","5a7a6c3577c881647d3c95c"]}"""

    val accessToken = AccessToken("2","no thing",payload,DateTime.now().toString("yyyy-MM-dd'T'HH:mm:ss"),"Access Token")
    val refreshToken = RefreshToken("5","no thing",DateTime.now().toString("yyyy-MM-dd'T'HH:mm:ss"),"Refresh Token")

    val genAccessToken  = JsonWebTokenUtility.createAccessToken(accessToken,config.get[String]("jwt.privateKey"))
    val genRefreshToken  = JsonWebTokenUtility.createRefreshTokenToken(refreshToken,config.get[String]("jwt.privateKey"))

    TokenCarrier(genAccessToken,genRefreshToken)
  }


}
