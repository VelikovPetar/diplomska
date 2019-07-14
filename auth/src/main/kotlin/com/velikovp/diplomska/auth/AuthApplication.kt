package com.velikovp.diplomska.auth

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
}

fun main(args: Array<String>) {
  runApplication<AuthApplication>(*args)
}