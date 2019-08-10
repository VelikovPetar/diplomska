package com.velikovp.diplomska.auth

import com.velikovp.diplomska.jwt.JwtTokenCreator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * Main application of the 'auth' micro-service.
 */
@SpringBootApplication
class AuthApplication {
  /**
   * Provides the default password encoder.
   *
   * @return the default password encoder.
   */
  @Bean
  fun bCryptPasswordEncoder() = BCryptPasswordEncoder()

  /**
   * Provides the default Jwt token creator.
   *
   * @return the default Jwt token creator.
   */
  @Bean
  fun jwtTokenCreator() = JwtTokenCreator()
}

fun main(args: Array<String>) {
  runApplication<AuthApplication>(*args)
}