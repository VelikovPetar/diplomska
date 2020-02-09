package com.velikovp.diplomska.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.Date

/**
 * Class handling the creation of JWT tokens.
 */
class JwtTokenCreator {

  companion object {
    private const val validityInMilliseconds = 3 * 60 * 60 * 1000
  }

  /**
   * Creates a JWT with the given subject
   *
   * @param subject, the provided subject.
   * @param base64Secret, the secret to use for signing the token.
   * @return the created JWT token as a String.
   */
  fun createToken(subject: String, base64Secret: String): String {
    return createToken(subject, mutableMapOf(), base64Secret)
  }

  /**
   * Creates a JWT with the given subject
   *
   * @param subject, the provided subject.
   * @param claims, a map containing key-value pairs to be included in the JWT body.
   * @param base64Secret, the secret to use for signing the token.
   * @return the created JWT token as a String.
   */
  fun createToken(subject: String, claims: MutableMap<String, Any>, base64Secret: String): String {
    val body = Jwts.claims(claims)
        .setSubject(subject)
    val date = Date()
    val validity = Date(date.time + validityInMilliseconds)
    return Jwts.builder()
        .setHeaderParam("typ", "JWT")
        .setClaims(body)
        .setIssuedAt(date)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, base64Secret)
        .compact()
  }
}