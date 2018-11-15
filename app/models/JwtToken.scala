package models

import play.api.libs.json.{Format, Json}


case class JwtToken(exp: Int, iss: String, sub: String, createdTime: String)

object JwtToken {
  implicit val jwtTokenFormat: Format[JwtToken] = Json.format[JwtToken]
}



