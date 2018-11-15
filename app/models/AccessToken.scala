package models

import play.api.libs.json.{Format, Json}

case class AccessToken (exp:String, iss:String, payload:String, createdAt:String,subject:String)

case class RefreshToken (exp:String, iss:String,createdAt:String, subject:String)

object AccessToken {
  implicit val AccessTokenFormat: Format[AccessToken] = Json.format[AccessToken]
}

object RefreshToken {
  implicit val RefreshTokenFormat: Format[RefreshToken] = Json.format[RefreshToken]
}