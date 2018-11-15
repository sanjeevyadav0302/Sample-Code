package models

import play.api.libs.json.{Format, Json}

case class TokenCarrier(accessToken:String, refreshToken:String)

object TokenCarrier {
  implicit val tokenGeneratorResponseFormat:Format[TokenCarrier] = Json.format[TokenCarrier]
}