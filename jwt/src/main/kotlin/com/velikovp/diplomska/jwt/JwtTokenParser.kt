package com.velikovp.diplomska.jwt

import io.jsonwebtoken.Jwts

/**
 * Class handling the parsing of JWT tokens.
 */
class JwtTokenParser {

  private val jwtTokenValidator = JwtTokenValidator()

  /**
   * Parses the body of the given JWT token.
   *
   * @param token, the JWT token.
   * @param base64Secret, the secret to use to parse the token.
   * @return map with the claims of the token, or an empty map if the token cannot be parsed.
   */
  fun parseClaims(token: String, base64Secret: String): Map<String, String> {
    if (jwtTokenValidator.validateToken(token, base64Secret) is JwtValidationResult.ResultOk) {
      val claims = Jwts.parser()
          .setSigningKey(base64Secret)
          .parseClaimsJws(token)
      val claimsMap = mutableMapOf<String, String>()
      claims.body.forEach { key, value ->
        claimsMap[key] = value.toString()
      }
      return claimsMap
    }
    return mapOf()
  }
}