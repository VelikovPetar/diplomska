package com.velikovp.diplomska.dispatcher.rest.controller

import com.velikovp.diplomska.dispatcher.rest.model.ResponseCode
import com.velikovp.diplomska.dispatcher.rest.model.ResponseModel
import com.velikovp.diplomska.jwt.JwtTokenValidator
import com.velikovp.diplomska.jwt.JwtValidationResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.Base64

/**
 * Controller handling the dispatching of code execution requests.
 */
@RestController
class DispatcherController(val jwtTokenValidator: JwtTokenValidator) {

  companion object {
    const val JWT_TOKEN_HEADER = "token"
  }

  @Value("\${jwt.token.secret}")
  lateinit var secretKey: String

  private val logger: Logger = LoggerFactory.getLogger(DispatcherController::class.java)

  /**
   * Base endpoint receiving the execution requests. Dispatches the requests to the appropriate executor. TODO(wip)
   */
  @RequestMapping("execute", method = [RequestMethod.GET])
  fun dispatchExecution(@RequestHeader headers: Map<String, String>): ResponseEntity<out ResponseModel> {
    val tokenVerificationResult = verifyAuthenticationToken(headers)
    logger.info("Token verification result: $tokenVerificationResult")
    return when (tokenVerificationResult) {
      is JwtValidationResult.ResultOk -> ResponseEntity.ok(ResponseModel())
      // todo update error handling
      else -> ResponseEntity.badRequest().body(ResponseModel(ResponseCode.BAD_REQUEST, "Invalid or missing auth token."))
    }
  }

  private fun verifyAuthenticationToken(headers: Map<String, String>): JwtValidationResult {
    val token = headers[JWT_TOKEN_HEADER]
    token ?: return JwtValidationResult.ResultJwtUnknownError()
    val base64encodedSecret = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    return jwtTokenValidator.validateToken(token, base64encodedSecret)
  }
}