package utility

import java.util.UUID

object Constants {

  val TOKEN_LENGTH = 45
  val SHA256 = "SHA-256"
  val TOKEN_CHARS = UUID.randomUUID().toString
  val UTF8 = "UTF-8"
  val AUTHORIZE = "/v1/authorize"
  val EXPIRY_TIME_IN_MINUTES = 20
  val JWT_EXPIRY_TIME_IN_MINUTES = 1

  val ACCESS_TOKEN_EXP = 5
  val REFRESH_TOKEN_EXP = 200
  val state = UUID.randomUUID().toString


}