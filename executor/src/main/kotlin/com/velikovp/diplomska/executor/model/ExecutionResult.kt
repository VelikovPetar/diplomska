package com.velikovp.diplomska.executor.model

/**
 * Model holding the results of a code execution.
 */
sealed class ExecutionResult {

  sealed class Error(open val message: String) : ExecutionResult() {
    data class Compilation(override val message: String) : Error(message)
    data class Runtime(override val message: String) : Error(message)
    data class Timeout(override val message: String) : Error(message)
    data class Unknown(override val message: String) : Error("Unknown error.")
  }

  sealed class Success : ExecutionResult() {
    data class OutputMismatch(val output: String, val expectedOutput: String) : Success()
    object OutputCorrect : Success()
  }
}