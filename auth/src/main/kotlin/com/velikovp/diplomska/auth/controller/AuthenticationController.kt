package com.velikovp.diplomska.auth.controller

import com.velikovp.diplomska.auth.model.ApplicationUser
import com.velikovp.diplomska.auth.repository.ApplicationUserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

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
  fun testRegister(@RequestParam email: String,
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
  @RequestMapping("/register", method = [RequestMethod.POST])
  fun register(@RequestBody applicationUser: ApplicationUser): Unit {
    applicationUser.password = passwordEncoder.encode(applicationUser.password)
    applicationUserRepository.save(applicationUser)
  }
}