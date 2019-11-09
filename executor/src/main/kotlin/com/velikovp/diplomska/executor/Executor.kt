package com.velikovp.diplomska.executor

import com.velikovp.diplomska.executor.model.ExecutionResult

/**
 * Defines the methods that a code-executor should support.
 */
interface Executor {

  /**
   * Executes the code with the given input, and validates the output against the expected output.
   *
   * @param solution, the submitted solution code.
   * @param input, the input of the program.
   * @param expectedOutput, the expected output for the given input.
   *
   * @return an [ExecutionResult] wrapping the result of the execution.
   */
  fun executeSingleTestCase(solution: String, input: String, expectedOutput: String): ExecutionResult
}