package utility

import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}

class TokenUtility {
  val JwtSecretAlgo = "HS256"

  def createToken(payload: String, oauthToken: String): String = {
    val header = JwtHeader(JwtSecretAlgo)
    val claimsSet = JwtClaimsSet(payload)
    JsonWebToken(header, claimsSet, oauthToken)
  }

  def validateToken(jwt: String, oauthToken: String): Boolean = {
    try {
      JsonWebToken.validate(jwt, oauthToken)
    } catch {
      case _: Throwable => false
    }
  }

  def parseTokenClaimUsername(jwt: String): Option[String] = {
    jwt match {
      case JsonWebToken(header, claimSet, signature) =>
        Some(claimSet.asSimpleMap.get("u"))
      case x => None
    }
  }

  def decode(jwtToken: String): String = {
    jwtToken match {
      case value if (value.contains(".")) => {
        val claimSetAsBytes = jwtToken.split("[.]")(1).getBytes("UTF-8")
        new String(java.util.Base64.getUrlDecoder.decode(claimSetAsBytes), "UTF-8")
      }
      case valueOfJwtToken => {
        new String(java.util.Base64.getUrlDecoder.decode(jwtToken.getBytes("UTF-8")), "UTF-8")
      }
    }
  }
}

object TokenUtility extends TokenUtility