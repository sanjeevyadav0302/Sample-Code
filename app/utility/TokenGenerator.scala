package utility

import java.security.{MessageDigest, SecureRandom}
import javax.inject.Inject


class TokenGenerator @Inject()(secureRandom: SecureRandom) {


  def generateSHAToken(tokenPrefix: String): String = {
    sha(tokenPrefix + System.nanoTime() + generateToken(Constants.TOKEN_LENGTH))
  }

  private def sha(s: String): String = {
    toHex(MessageDigest.getInstance(Constants.SHA256).digest(s.getBytes(Constants.UTF8)))
  }

  private def toHex(bytes: Array[Byte]): String = bytes.map("%02x".format(_)).mkString("")

  private def generateToken(tokenLength: Int): String = {
    val charLen = Constants.TOKEN_CHARS.length()

    def generateTokenAccumulator(accumulator: String, number: Int): String = {
      if (number == 0) return accumulator
      else
        generateTokenAccumulator(accumulator + Constants.TOKEN_CHARS(secureRandom.nextInt(charLen)).toString, number - 1)
    }

    generateTokenAccumulator("", tokenLength)
  }

}
