package com.velikovp.diplomska.dispatcher

import com.velikovp.diplomska.executor.Executor
import com.velikovp.diplomska.executor.java.JavaExecutor
import com.velikovp.diplomska.executor.python.PythonExecutor
import com.velikovp.diplomska.jwt.JwtTokenParser
import com.velikovp.diplomska.jwt.JwtTokenValidator
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class DispatcherApplication {

    /**
     * Provides the default Jwt token validator.
     */
    @Bean
    fun jwtTokenValidator() = JwtTokenValidator()

    /**
     * Provides the default Jwt token parser.
     */
    @Bean
    fun jwtTokenParser() = JwtTokenParser()

    /**
     * Provides the [PythonExecutor].
     */
    @Bean
    @Qualifier("pythonExecutor")
    fun pythonExecutor(): Executor = PythonExecutor()

    @Bean
    @Qualifier("javaExecutor")
    fun javaExecutor(): Executor = JavaExecutor()
}

fun main(args: Array<String>) {
    runApplication<DispatcherApplication>(*args)
}