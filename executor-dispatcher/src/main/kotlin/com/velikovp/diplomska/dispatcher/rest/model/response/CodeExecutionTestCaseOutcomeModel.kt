package com.velikovp.diplomska.dispatcher.rest.model.response

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Model representing a code execution outcome for a test case.
 *
 * @param input, the input for the given test case.
 * @param output, the output of the submitted solution for the given test case.
 * @param expectedOutput, the expected output for the given test case.
 */
data class CodeExecutionTestCaseOutcomeModel(
  @field:JsonProperty("input")
  val input: String,

  @field:JsonProperty("output")
  val output: String,

  @field:JsonProperty("expectedOutput")
  val expectedOutput: String
)