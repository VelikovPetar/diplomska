package com.velikovp.diplomska.auth.rest.model.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

/**
 * Model representing the required request body for the registerUser call.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class RegisterUserRequestModel(
  @field:Email(message = "Email must be a well-formed address.")
  @field:NotBlank(message = "Email cannot be empty.")
  @field:JsonProperty("email")
  val email: String?,

  @field:NotBlank(message = "Name cannot be empty.")
  @field:JsonProperty("name")
  val name: String?,

  @field:NotBlank(message = "Surname cannot be empty.")
  @field:JsonProperty("surname")
  val surname: String?,

  @field:NotBlank(message = "Password cannot be empty.")
  @field:JsonProperty("password")
  val password: String?
)