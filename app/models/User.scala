package models

import play.api.libs.json.{Format, Json}


case class User(id: Option[String],
                email: String,
                firstName: String,
                lastName: Option[String],
                user_group: Option[String],
                roles: List[String] = List.empty)

object User {
  implicit val userFormat: Format[User] = Json.format[User]
}

