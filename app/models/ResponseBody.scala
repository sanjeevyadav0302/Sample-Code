package models

import play.api.libs.json.{Format, Json}

case class ResponseBody(response: String)

object ResponseBody {
  implicit val format: Format[ResponseBody] = Json.format[ResponseBody]
}