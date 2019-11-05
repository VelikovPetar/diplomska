package com.velikovp.diplomska.dispatcher.rest.model.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotEmpty

/**
 * Model representing the JSON format of a test case for a task.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TestCaseModel(
  @field:NotEmpty(message = "The input cannot be empty.")
  @field:JsonProperty("input")
  val input: String?,

  @field:NotEmpty(message = "The expected output cannot be empty.")
  @field:JsonProperty("expectedOutput")
  val expectedOutput: String?
)