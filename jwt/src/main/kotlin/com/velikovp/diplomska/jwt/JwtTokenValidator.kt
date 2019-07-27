package com.velikovp.diplomska.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException

/**
 * Class handling the verification of JWT tokens.
 */
class JwtTokenValidator {

  /**
   * Attempts to perform validation of a JWT token.
   *
   * @param token, the JWT token.
   * @param base64Secret, the secret to use to parse the token.
   * @return result from the performed validation.
   */
  fun validateToken(token: String, base64Secret: String): JwtValidationResult {
    try {
      Jwts.parser()
          .setSigningKey(base64Secret)
          .parseClaimsJws(token)
      return JwtValidationResult.ResultOk()
    } catch (e: Exception) {
      return mapJwtParsingError(e)
    }
  }

  private fun mapJwtParsingError(e: Exception): JwtValidationResult {
    return when (e) {
      is ExpiredJwtException -> JwtValidationResult.ResultJwtExpired()
      is UnsupportedJwtException -> JwtValidationResult.ResultJwtUnsupported()
      is MalformedJwtException -> JwtValidationResult.ResultJwtMalformed()
      is SignatureException -> JwtValidationResult.ResultJwtInvalidSignature()
      else -> JwtValidationResult.ResultJwtUnknownError()
    }
  }
}