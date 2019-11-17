package com.velikovp.diplomska.executor.python

import com.velikovp.diplomska.executor.Executor
import com.velikovp.diplomska.executor.cmd.Cmd
import com.velikovp.diplomska.executor.cmd.CmdResult
import com.velikovp.diplomska.executor.model.ExecutionResult
import java.io.File

/**
 * Implementation of the [Executor] interface, handling execution of python code.
 */
class PythonExecutor : Executor {

  override fun executeSingleTestCase(filename: String, solution: String, tmpDir: String, input: String, expectedOutput: String): ExecutionResult {
    // Create tmp dir
    val dir = File(tmpDir)
    if (!dir.exists()) {
      dir.mkdirs()
    }
    // Create tmp .py file
    val fullPath = "$tmpDir/$filename"
    val writeToFileResult = Cmd.writeToFile(solution, fullPath)
    if (writeToFileResult is CmdResult.Error) {
      return ExecutionResult.Error.Unknown(writeToFileResult.message)
    }
    // Execute .py file
    val solutionExecutionResult = Cmd.executeCommand("python3 $fullPath $input")
    if (solutionExecutionResult is CmdResult.Error) {
      return ExecutionResult.Error.Runtime(solutionExecutionResult.message)
    }
    // Evaluate output against the expected output
    val output = (solutionExecutionResult as CmdResult.Success).output
    return if (output != expectedOutput) {
      ExecutionResult.Success.OutputMismatch(output, expectedOutput)
    } else {
      ExecutionResult.Success.OutputCorrect
    }
  }
}