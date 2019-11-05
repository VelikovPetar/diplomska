package com.velikovp.diplomska.auth.rest.error

import com.velikovp.diplomska.auth.rest.model.response.ResponseCode
import com.velikovp.diplomska.auth.rest.model.response.ResponseModel
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * Handles the errors in the JSON request models.
 */
@ControllerAdvice
class AuthenticationExceptionHandler : ResponseEntityExceptionHandler() {

  override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException,
                                            headers: HttpHeaders,
                                            status: HttpStatus,
                                            request: WebRequest): ResponseEntity<Any> {
    val errorMessage = ex.bindingResult.fieldError?.let {
      "Error in field '${it.field}' : ${it.defaultMessage}"
    } ?: "Unknown exception."
    return ResponseEntity.badRequest()
      .body(ResponseModel(ResponseCode.BAD_REQUEST, errorMessage))
  }
}