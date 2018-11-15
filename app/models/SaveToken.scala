package models

import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

case class SaveToken(id: Option[Long] = None,
                     clientId: String,
                     oauthToken: String,
                     currentDate: DateTime,
                     refreshToken: String
                    )

object SaveToken {
  implicit val errorResponseResponseFormat: Format[SaveToken] = Json.format[SaveToken]
}
