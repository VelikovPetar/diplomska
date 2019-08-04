package com.velikovp.diplomska.auth.rest.controller

import com.velikovp.diplomska.auth.model.ApplicationUser
import com.velikovp.diplomska.auth.repository.ApplicationUserRepository
import com.velikovp.diplomska.auth.rest.model.request.RegisterUserRequestModel
import com.velikovp.diplomska.auth.rest.model.response.RegisterUserResponseModel
import com.velikovp.diplomska.auth.rest.model.response.ResponseModel
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.Email

/**
 * Controller handling the user authentication. (Registration and login)
 */
@RestController
class AuthenticationController(val applicationUserRepository: ApplicationUserRepository,
                               val passwordEncoder: BCryptPasswordEncoder) {

  /**
   * Testing endpont.
   * todo: remove
   */
  @RequestMapping("/testRegister", method = [RequestMethod.GET])
  fun testRegister(@Email @RequestParam email: String,
                   @RequestParam name: String,
                   @RequestParam surname: String,
                   @RequestParam password: String): String {
    val user = ApplicationUser(email, name, surname, passwordEncoder.encode(password))
    applicationUserRepository.save(user)
    return "Success"
  }

  /**
   * Endpoint for user registration.
   * todo: return response, validate input, define roles...
   */
  @RequestMapping("register", method = [RequestMethod.POST])
  fun register(@Valid @RequestBody registerUserRequest: RegisterUserRequestModel): ResponseEntity<in ResponseModel> {
    val user = ApplicationUser(
      registerUserRequest.email!!,
      registerUserRequest.name!!,
      registerUserRequest.surname!!,
      passwordEncoder.encode(registerUserRequest.password)
    )
    applicationUserRepository.save(user)
    return ResponseEntity.ok(RegisterUserResponseModel())
  }
}