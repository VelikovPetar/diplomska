package com.velikovp.diplomska.auth.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

/**
 * Configuration of the Spring Web Security. Disables the default authentication requirements
 * for all requests, as the authentication is handled manually.
 */
@Configuration
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

  override fun configure(http: HttpSecurity?) {
    http?.let {
      it.csrf().disable()
      it.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      it.authorizeRequests().anyRequest().permitAll()
      // todo: further configuration
    }
  }
}