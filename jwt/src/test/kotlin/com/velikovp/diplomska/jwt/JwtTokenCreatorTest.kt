package com.velikovp.diplomska.jwt

import org.junit.Assert
import org.junit.Test
import java.util.Base64

class JwtTokenCreatorTest {

  private val jwtTokenCreator = JwtTokenCreator()
  private val jwtTokenParser = JwtTokenParser()

  @Test
  fun testSubjectOnly() {
    // given
    val subject = "testSubject1"
    val secretKey = Base64.getEncoder().encodeToString("secretKey".toByteArray())
    //when
    val token = jwtTokenCreator.createToken(subject, secretKey)
    val claims = jwtTokenParser.parseClaims(token, secretKey)
    // then
    Assert.assertEquals(3, claims.size)
    Assert.assertTrue(claims.containsKey("sub"))
    Assert.assertTrue(claims.containsKey("iat"))
    Assert.assertTrue(claims.containsKey("exp"))
    Assert.assertEquals("testSubject1", claims["sub"])
  }

  @Test
  fun testSubjectAndCustomClaims() {
    //given
    val subject = "testSubject1"
    val customClaims = mutableMapOf("key1" to "value1", "key2" to 2)
    val secretKey = Base64.getEncoder().encodeToString("secretKey".toByteArray())
    //when
    val token = jwtTokenCreator.createToken(subject, customClaims, secretKey)
    val claims = jwtTokenParser.parseClaims(token, secretKey)
    // then
    Assert.assertEquals(5, claims.size)
    Assert.assertTrue(claims.containsKey("sub"))
    Assert.assertTrue(claims.containsKey("iat"))
    Assert.assertTrue(claims.containsKey("exp"))
    Assert.assertTrue(claims.containsKey("key1"))
    Assert.assertTrue(claims.containsKey("key2"))
    Assert.assertEquals("testSubject1", claims["sub"])
    Assert.assertEquals("value1", claims["key1"].toString())
    Assert.assertEquals("2", claims["key2"].toString())
  }
}