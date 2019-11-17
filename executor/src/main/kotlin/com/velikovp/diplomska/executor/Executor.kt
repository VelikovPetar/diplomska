package com.velikovp.diplomska.executor

import com.velikovp.diplomska.executor.model.ExecutionResult
import java.io.File

/**
 * Defines the methods that a code-executor should support.
 */
interface Executor {

  /**
   * Executes the code with the given input, and validates the output against the expected output.
   *
   * @param filename, the name of the solution file.
   * @param solution, the submitted solution code.
   * @param tmpDir, path of the directory to store intermediate results.
   * @param input, the input of the program.
   * @param expectedOutput, the expected output for the given input.
   *
   * @return an [ExecutionResult] wrapping the result of the execution.
   */
  fun executeSingleTestCase(filename: String, solution: String, tmpDir: String, input: String, expectedOutput: String): ExecutionResult

  /**
   * Cleans up the intermediate results in the tmpDir.
   *
   * @param tmpDir, the dir to clean up.
   */
  fun cleanUp(tmpDir: String) {
    val dir = File(tmpDir)
    dir.deleteRecursively()
  }
}