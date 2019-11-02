package com.velikovp.diplomska.dispatcher

import com.velikovp.diplomska.jwt.JwtTokenParser
import com.velikovp.diplomska.jwt.JwtTokenValidator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class DispatcherApplication {

    /**
     * Provides the default Jwt token validator.
     *
     * @return the default Jwt token validator.
     */
    @Bean
    fun jwtTokenValidator() = JwtTokenValidator()

    /**
     * Provides the default Jwt token parser.
     *
     * @return the default Jwt token parser.
     */
    @Bean
    fun jwtTokenParser() = JwtTokenParser()
}

fun main(args: Array<String>) {
    runApplication<DispatcherApplication>(*args)
}