package com.velikovp.diplomska.executor.model

/**
 * Class representing the possible outcomes of an execution of a CMD command.
 */
sealed class CmdResult {
  data class Success(val output: String) : CmdResult()
  data class Error(val message: String) : CmdResult()
}