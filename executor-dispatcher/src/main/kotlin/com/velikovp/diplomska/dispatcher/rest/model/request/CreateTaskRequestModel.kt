package com.velikovp.diplomska.dispatcher.rest.model.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

/**
 * Model representing the required body of the request for creating a new task.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class CreateTaskRequestModel(
  @field:NotEmpty(message = "The description of the task cannot be empty.")
  @field:JsonProperty("taskDescription")
  val taskDescription: String?,

  @field:Valid
  @field:NotEmpty(message = "The list of test cases cannot be empty.")
  @field:Size(min = 1, max = 20, message = "The list of test cases must be between 1 and 20 included.")
  @field:JsonProperty("testCases")
  val testCases: List<@Valid TestCaseModel>?
)