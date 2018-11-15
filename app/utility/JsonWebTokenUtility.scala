
package utility

import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import com.google.common.base.Charsets
import com.google.common.io.BaseEncoding
import models.{AccessToken, RefreshToken}
import org.joda.time.DateTime


class JsonWebTokenUtility {

  val JwtSecretAlgorithm = "HS256"

  def createAccessToken(accessToken:AccessToken, secretKey:String): String ={
    val createdTime = DateTime.now.toLocalDateTime
    val claims = Map("exp" -> accessToken.exp,"iss"->accessToken.iss, "createdAt" -> accessToken.createdAt,"payload" -> accessToken.payload,"subject" -> accessToken.subject)
    val claimsSet = JwtClaimsSet(claims)
    val header = JwtHeader(JwtSecretAlgorithm)
    JsonWebToken(header, claimsSet, secretKey)
  }

  def createRefreshTokenToken(refreshToken:RefreshToken, secretKey:String): String ={
    val createdTime = DateTime.now.toLocalDateTime
    val claims = Map("exp" -> refreshToken.exp,"iss"->refreshToken.iss, "createdAt" -> refreshToken.createdAt,"subject" -> refreshToken.subject)
    val claimsSet = JwtClaimsSet(claims)
    val header = JwtHeader(JwtSecretAlgorithm)
    JsonWebToken(header, claimsSet, secretKey)
  }

}

object JsonWebTokenUtility extends JsonWebTokenUtility