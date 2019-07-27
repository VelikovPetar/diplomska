package com.velikovp.diplomska.jwt

import org.junit.Assert
import org.junit.Test
import java.util.Base64

class JwtTokenParserTest {

  val jwtTokenParser = JwtTokenParser()

  @Test
  fun parseSubject() {
    // given
    val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0U3ViamVjdDEiLCJpYXQiOjE1MTYyMzkwMjJ9.sFM_mSb8b143eDJzCCdhQNRx12s0t6IUN4AVWko5ArY"
    val secretKey = Base64.getEncoder().encodeToString("secretKey".toByteArray())
    val expectedSubject = "testSubject1"
    val expectedIssuedAt = 1516239022
    // when
    val claims = jwtTokenParser.parseClaims(token, secretKey)
    // then
    Assert.assertEquals(2, claims.size)
    Assert.assertTrue(claims.containsKey("sub"))
    Assert.assertTrue(claims.containsKey("iat"))
    Assert.assertEquals(expectedSubject, claims["sub"])
    Assert.assertEquals(expectedIssuedAt.toString(), claims["iat"].toString())
  }
}