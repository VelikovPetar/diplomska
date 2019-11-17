package com.velikovp.diplomska.executor.java

import com.velikovp.diplomska.executor.Executor
import com.velikovp.diplomska.executor.cmd.Cmd
import com.velikovp.diplomska.executor.cmd.CmdResult
import com.velikovp.diplomska.executor.model.ExecutionResult
import java.io.File

/**
 * Implementation of the [Executor] interface, handling execution of python code.
 */
class JavaExecutor: Executor {

  override fun executeSingleTestCase(filename: String, solution: String, tmpDir: String, input: String, expectedOutput: String): ExecutionResult {
    // Create tmp dir
    val dir = File(tmpDir)
    if (!dir.exists()) {
      dir.mkdirs()
    }
    // Create tmp .java file in the tmp dir
    val fullPath = "$tmpDir/$filename"
    val writeToFileResult = Cmd.writeToFile(solution, fullPath)
    if (writeToFileResult is CmdResult.Error) {
      return ExecutionResult.Error.Unknown(writeToFileResult.message)
    }
    // Compile the .java file
    // If the compiled .class file exists, skip this step
    val className = filename.replace(".java", "")
    if (!File("$tmpDir/$className.class").exists()) {
      val compilationResult = Cmd.executeCommand("javac $fullPath")
      if (compilationResult is CmdResult.Error) {
        return ExecutionResult.Error.Compilation("Failed to compile solution. Output: ${compilationResult.message}")
      }
    }
    // Execute the generated .class file
    val executionResult = Cmd.executeCommand("java -cp $tmpDir $className $input")
    if (executionResult is CmdResult.Error) {
      return ExecutionResult.Error.Runtime("Runtime error during solution execution. Output: ${executionResult.message}")
    }
    // Evaluate output against the expected output
    val output = (executionResult as CmdResult.Success).output
    return if (output != expectedOutput) {
      ExecutionResult.Success.OutputMismatch(output, expectedOutput)
    } else {
      ExecutionResult.Success.OutputCorrect
    }
  }
}