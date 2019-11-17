package com.velikovp.diplomska.executor.cmd

import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Object abstracting the logic of execution of command line commands.
 */
object Cmd {

  private const val COMMAND_ECHO = "echo"

  /**
   * Writes content to a file in the local filesystem.
   *
   * @param content, the content to write.
   * @param filename, the name of the file.
   * @return [CmdResult], the output of the execution.
   */
  fun writeToFile(content: String, filename: String): CmdResult {
    val process = ProcessBuilder(COMMAND_ECHO, content)
      .redirectOutput(File(filename))
      .start()
    val processOutput = readStdOutputFromProcess(process)
    val processError = readStdErrorFormProcess(process)
    val exitCode = process.waitFor()
    if (exitCode != 0) {
      return CmdResult.Error("Failed to save '$filename'. Output: $processError.", exitCode)
    }
    return CmdResult.Success(processOutput)
  }

  /**
   * Executes the given command.
   *
   * @param command, the command to execute.
   * @return [CmdResult], the output of the execution.
   */
  fun executeCommand(command: String): CmdResult {
    val process = Runtime.getRuntime().exec(command)
    val processOutput = readStdOutputFromProcess(process)
    val processError = readStdErrorFormProcess(process)
    val exitCode = process.waitFor()
    if (exitCode != 0) {
      return CmdResult.Error("Failed to execute command: '$command'. Output: $processError.", exitCode)
    }
    return CmdResult.Success(processOutput)
  }

  private fun readStdOutputFromProcess(process: Process): String {
    return readOutputFromStream(process.inputStream)
  }

  private fun readStdErrorFormProcess(process: Process): String {
    return readOutputFromStream(process.errorStream)
  }

  private fun readOutputFromStream(inputStream: InputStream): String {
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    return bufferedReader.readLines().joinToString(separator = "\n")
  }
}