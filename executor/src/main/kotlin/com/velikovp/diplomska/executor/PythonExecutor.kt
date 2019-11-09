package com.velikovp.diplomska.executor

import com.velikovp.diplomska.executor.model.CmdResult
import com.velikovp.diplomska.executor.model.ExecutionResult
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * Implementation of the [Executor] interface, handling execution of python code.
 */
class PythonExecutor : Executor {

  companion object {
    private const val SOLUTION_FILE_TMP_NAME = "solution.py"
  }

  override fun executeSingleTestCase(solution: String, input: String, expectedOutput: String): ExecutionResult {
    // TODO refactor this
    val process = ProcessBuilder("echo", solution)
      .redirectOutput(File("solution.py"))
      .start()
    val exitCode = process.waitFor()
    if (exitCode != 0) {
      return ExecutionResult.Error.Unknown("Error saving file solution.py")
    }

    val writeSolutionToFileResult = executeCmd(commandWriteSolutionToFile(solution))
    if (writeSolutionToFileResult is CmdResult.Error) {
      return ExecutionResult.Error.Unknown(writeSolutionToFileResult.message)
    }
    val solutionExecutionResult = executeCmd(commandExecute(input))
    if (solutionExecutionResult is CmdResult.Error) {
      return ExecutionResult.Error.Runtime(solutionExecutionResult.message)
    }
    val output = (solutionExecutionResult as CmdResult.Success).output
    if (output != expectedOutput) {
      return ExecutionResult.Success.OutputMismatch(output, expectedOutput)
    } else {
      return ExecutionResult.Success.OutputCorrect
    }
  }

  private fun executeCmd(command: String): CmdResult {
    val runtime = Runtime.getRuntime()
    val process = runtime.exec(command)
    val stdOutput = BufferedReader(InputStreamReader(process.inputStream))
    val stdError = BufferedReader(InputStreamReader(process.errorStream))

    val output = stdOutput.readLines().joinToString(separator = "\n")
    val error = stdError.readLines().joinToString(separator = "\n")
    val exitCode = process.waitFor()
    return if (exitCode == 0) {
      CmdResult.Success(output)
    } else {
      CmdResult.Error(error)
    }
  }

  private fun commandWriteSolutionToFile(solution: String) = "echo \"$solution\" >> $SOLUTION_FILE_TMP_NAME"

  private fun commandExecute(input: String) = "python3 $SOLUTION_FILE_TMP_NAME $input"
}