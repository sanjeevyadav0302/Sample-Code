package models

import play.api.libs.json.{Format, Json}

case class AuthenticateServiceResponse (url:String, state: String)


object AuthenticateServiceResponse {
  implicit val format: Format[AuthenticateServiceResponse] = Json.format[AuthenticateServiceResponse]
}