package com.velikovp.diplomska.auth.rest.controller

import com.velikovp.diplomska.auth.model.ApplicationUser
import com.velikovp.diplomska.auth.repository.ApplicationUserRepository
import com.velikovp.diplomska.auth.rest.model.request.RegisterUserRequestModel
import com.velikovp.diplomska.auth.rest.model.response.ResponseCode
import com.velikovp.diplomska.auth.rest.model.response.ResponseModel
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * Controller handling the user authentication. (Registration and login)
 */
@RestController
class AuthenticationController(val applicationUserRepository: ApplicationUserRepository,
                               val passwordEncoder: BCryptPasswordEncoder) {

  /**
   * Endpoint for user registration.
   */
  @RequestMapping("register", method = [RequestMethod.POST])
  fun register(@Valid @RequestBody registerUserRequest: RegisterUserRequestModel): ResponseEntity<in ResponseModel> {
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

  private fun emailExists(email: String): Boolean {
    return applicationUserRepository.findByEmail(email) != null
  }
}