package utility


import models._
import org.joda.time.DateTime
import play.api.libs.json.{JsError, JsSuccess, Json}

object ValidateOauth {

  def parseToken(jwtToken: String): Option[JwtToken] = {
    val json = Json.parse(TokenUtility.decode(jwtToken).replace("|", ""))
    json.validate[JwtToken] match {
      case success: JsSuccess[JwtToken] => {
        success.asOpt
      }
      case err: JsError => println("Invalid jwtToken" + err); None
    }
  }

  def parseAccessToken(accessToken: String): Option[AccessToken] = {
    val json = Json.parse(TokenUtility.decode(accessToken).replace("|", ""))
    json.validate[AccessToken] match {
      case success: JsSuccess[AccessToken] => {
        success.asOpt
      }
      case err: JsError => println("Invalid jwtToken" + err); None
    }
  }

  def parseRefreshToken(refreshToken: String): Option[RefreshToken] = {
    val json = Json.parse(TokenUtility.decode(refreshToken).replace("|", ""))
    json.validate[RefreshToken] match {
      case success: JsSuccess[RefreshToken] => {
        success.asOpt
      }
      case err: JsError => println("Invalid jwtToken" + err); None
    }
  }

  //Method name was compareWithCurrentDateAndReturnIfExpired
  def isExpired(createdDateTime: String, expireInMin: Int): Boolean = {
    DateTime.parse(createdDateTime).plusMinutes(expireInMin).compareTo(new DateTime()) match {
      case 1 => false // if current time is < expiry dateTime
      case -1 => true  // if current time is > expiry dateTime
    }
  }

  def parseAccessTokenJson(accessTokenJson: String): Option[SaveToken] = {
    Json.parse(accessTokenJson).validate[SaveToken] match {
      case success: JsSuccess[SaveToken] => Some(success.get)
      case err: JsError => println("Invalid Json"); None
    }
  }

  def addMinutesInDateTime(dateTime: String, minToAdd: Int): String = {
    DateTime.parse(dateTime).plusMinutes(minToAdd).toString("yyyy-MM-dd'T'HH:mm:ss")
  }

  def parsePayload(userJsonJwt: String): Option[User] = {
    Json.parse(userJsonJwt).validate[User] match {
      case success: JsSuccess[User] => success.asOpt
      case err: JsError => println("Invalid userJsonJwt" + err); None
    }
  }
}