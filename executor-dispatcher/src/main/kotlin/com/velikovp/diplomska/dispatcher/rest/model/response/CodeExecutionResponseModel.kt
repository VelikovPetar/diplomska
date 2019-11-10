package com.velikovp.diplomska.dispatcher.rest.model.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.velikovp.diplomska.dispatcher.rest.model.ResponseModel

/**
 * Model representing the response of a code execution for all test cases.
 *
 * @param taskId, the Id of the task for which the solution was submitted.
 * @param testCases, the list of outputs for each test case for the submitted solution.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class CodeExecutionResponseModel(
  @field:JsonProperty("taskId")
  val taskId: Long,

  @field:JsonProperty("testCases")
  val testCases: List<CodeExecutionTestCaseOutcomeModel>?
) : ResponseModel()