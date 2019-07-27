package com.velikovp.diplomska.jwt

import org.junit.Assert
import org.junit.Test
import java.util.Base64

class JwtTokenValidatorTest {

  private val jwtTokenValidator = JwtTokenValidator()

  @Test
  fun testValidToken() {
    // given
    val validTokenWithoutExp =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0U3ViamVjdDEiLCJpYXQiOjE1NjQyMjgyNzF9.p3TnUHs9BbFHF6caUM6tgGOfzcxuN_wjIOGcts2pB3M"
    val base64Secret = Base64.getEncoder().encodeToString("secretKey".toByteArray())
    // when
    val result = jwtTokenValidator.validateToken(validTokenWithoutExp, base64Secret)
    // then
    Assert.assertTrue(result is JwtValidationResult.ResultOk)
  }

  @Test
  fun testExpiredToken() {
    // given
    val expiredToken =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0U3ViamVjdDEiLCJpYXQiOjE1NjQyMjgyNzEsImV4cCI6MTU2NDIyODM3MX0.IsmwHDFV60eumPYXGrmn39MQ2a8wPGboFMffXPL6oRw"
    val base64Secret = Base64.getEncoder().encodeToString("secretKey".toByteArray())
    // when
    val result = jwtTokenValidator.validateToken(expiredToken, base64Secret)
    // then
    Assert.assertTrue(result is JwtValidationResult.ResultJwtExpired)
  }

  @Test
  fun testMalformedToken_MissingHeader() {
    // given
    val malformedToken =
        "eyJzdWIiOiJ0ZXN0U3ViamVjdDEiLCJpYXQiOjE1NjQyMjgyNzEsImV4cCI6MTU2NDIyODM3MX0.IsmwHDFV60eumPYXGrmn39MQ2a8wPGboFMffXPL6oRw"
    val base64Secret = Base64.getEncoder().encodeToString("secretKey".toByteArray())
    // when
    val result = jwtTokenValidator.validateToken(malformedToken, base64Secret)
    // then
    Assert.assertTrue(result is JwtValidationResult.ResultJwtMalformed)
  }

  @Test
  fun testMalformedToken_MissingBody() {
    // given
    val malformedToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.IsmwHDFV60eumPYXGrmn39MQ2a8wPGboFMffXPL6oRw"
    val base64Secret = Base64.getEncoder().encodeToString("secretKey".toByteArray())
    // when
    val result = jwtTokenValidator.validateToken(malformedToken, base64Secret)
    // then
    Assert.assertTrue(result is JwtValidationResult.ResultJwtMalformed)
  }

  @Test
  fun testInvalidSignatureToken() {
    // given
    val tokenSignedWithDifferentKey =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0U3ViamVjdDEiLCJpYXQiOjE1NjQyMjgyNzEsImV4cCI6MTU2NDIyODM3MX0.DG4iZoJxAbpBvQdTHiYN8o-OOshIr5nVq3ZIh3CtUCM"
    val base64Secret = Base64.getEncoder().encodeToString("secretKey".toByteArray())
    // when
    val result = jwtTokenValidator.validateToken(tokenSignedWithDifferentKey, base64Secret)
    // then
    Assert.assertTrue(result is JwtValidationResult.ResultJwtInvalidSignature)
  }
}