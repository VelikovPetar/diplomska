package com.velikovp.diplomska.auth.rest.controller

import com.velikovp.diplomska.auth.database.entity.ApplicationUser
import com.velikovp.diplomska.auth.database.repository.ApplicationUserRepository
import com.velikovp.diplomska.auth.rest.model.request.LoginRequestModel
import com.velikovp.diplomska.auth.rest.model.request.RegisterUserRequestModel
import com.velikovp.diplomska.auth.rest.model.response.ResponseCode
import com.velikovp.diplomska.auth.rest.model.response.ResponseModel
import com.velikovp.diplomska.jwt.JwtTokenCreator
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid

/**
 * Controller handling the user authentication. (Registration and login)
 */
@RestController
class AuthenticationController(val applicationUserRepository: ApplicationUserRepository,
                               val passwordEncoder: BCryptPasswordEncoder,
                               val jwtTokenCreator: JwtTokenCreator) {

  @Value("\${jwt.token.secret}")
  lateinit var secretKey: String

  /**
   * Endpoint for user registration.
   *
   * @param registerUserRequest, the request model with the registration credentials.
   */
  @RequestMapping("register", method = [RequestMethod.POST])
  fun register(@Valid @RequestBody registerUserRequest: RegisterUserRequestModel): ResponseEntity<out ResponseModel> {
    val user = ApplicationUser(
      registerUserRequest.email!!,
      registerUserRequest.name!!,
      registerUserRequest.surname!!,
      passwordEncoder.encode(registerUserRequest.password)
    )
    if (emailExists(registerUserRequest.email)) {
      return ResponseEntity.badRequest()
        .body(ResponseModel(ResponseCode.EMAIL_EXISTS, "Email already registered."))
    }
    applicationUserRepository.save(user)
    return ResponseEntity.ok(ResponseModel())
  }

  /**
   * Endpoint for user login.
   *
   * @param loginRequestModel, the request model with the login credentials.
   */
  @RequestMapping("/login", method = [RequestMethod.POST])
  fun login(@Valid @RequestBody loginRequestModel: LoginRequestModel): ResponseEntity<out ResponseModel> {
    val email = loginRequestModel.email!!
    val applicationUser = applicationUserRepository.findByEmail(email) ?: return invalidCredentialsResponse()
    if (!passwordEncoder.matches(loginRequestModel.password!!, applicationUser.password)) {
      return invalidCredentialsResponse()
    }
    val userId = applicationUser.getId() ?: return unknownErrorResponse()
    val secret = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    return ResponseEntity.ok()
      .header("TOKEN", jwtTokenCreator.createToken(userId.toString(), secret))
      .body(ResponseModel())
  }

  private fun emailExists(email: String): Boolean {
    return applicationUserRepository.findByEmail(email) != null
  }

  private fun invalidCredentialsResponse(): ResponseEntity<ResponseModel> {
    return ResponseEntity.badRequest()
      .body(ResponseModel(ResponseCode.INVALID_CREDENTIALS, "Unknown email or incorrect password."))
  }

  private fun unknownErrorResponse(): ResponseEntity<ResponseModel> {
    return ResponseEntity.badRequest()
      .body(ResponseModel(ResponseCode.UNKNOWN_ERROR, "An unexpected error has occurred."))
  }
}