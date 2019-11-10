package com.velikovp.diplomska.dispatcher.rest.model.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.velikovp.diplomska.dispatcher.rest.model.ResponseModel

/**
 * Model representing the response for createTask requests.
 */
data class CreateTaskResponseModel(
  @field:JsonProperty("taskId")
  val taskId: Long
): ResponseModel()