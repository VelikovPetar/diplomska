package com.velikovp.diplomska.dispatcher.service

import com.velikovp.diplomska.dispatcher.database.entity.Solution
import com.velikovp.diplomska.dispatcher.database.entity.SolutionTestCase
import com.velikovp.diplomska.dispatcher.database.entity.Task
import com.velikovp.diplomska.dispatcher.database.repository.SolutionTestCaseRepository
import com.velikovp.diplomska.dispatcher.database.repository.SolutionsRepository
import com.velikovp.diplomska.dispatcher.database.repository.TasksRepository
import com.velikovp.diplomska.dispatcher.service.utils.FileUtils
import com.velikovp.diplomska.executor.Executor
import com.velikovp.diplomska.executor.model.ExecutionResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

/**
 * Service holding the logic for storing/retrieving [Solution]s from the storage(Database).
 */
@Service
class SolutionsService(
  @Qualifier("pythonExecutor") private val executor: Executor,
  private val solutionsRepository: SolutionsRepository,
  private val tasksRepository: TasksRepository,
  private val solutionTestCaseRepository: SolutionTestCaseRepository
) {

  private val logger: Logger = LoggerFactory.getLogger(SolutionsService::class.java)

  /**
   * Stores a solution in the storage(database).
   *
   * @param file, the uploaded solution.
   * @param language, the programming language in which the solution is written in.
   * @param taskId, the Id of the task that the solution is submitted for.
   * @param submittedBy, the Id of the user which submitted the solution.
   *
   * @return the [Solution] instance if it was successfully stored.
   * @throws [StorageException] if the storing of the solution fails.
   */
  fun storeSolution(file: MultipartFile, language: String, taskId: Long, submittedBy: Long): Solution {
    val task = try {
      tasksRepository.getOne(taskId)
    } catch (e: Exception) {
      logger.debug("Task with id: $taskId not found.")
      null
    }
    task ?: throw StorageException("Task with id: $taskId not found.")
    val solutionContent = FileUtils.readContentsFromMultipartFile(file)
    val solution = Solution(
      filename = file.originalFilename ?: "solution_for_task_$taskId.py",
      fileContentType = file.contentType ?: "",
      language = language,
      userId = submittedBy,
      task = task,
      content = solutionContent
    )
    val savedSolution = solutionsRepository.save(solution)
    savedSolution.solutionTestCases = evaluateSolutionAgainstTestCases(savedSolution, task).map {
      solutionTestCaseRepository.save(it)
    }
    return try {
      solutionsRepository.save(savedSolution)
    } catch (e: Exception) {
      logger.debug("Error saving task execution outputs.", e)
      throw StorageException("Error saving task execution outputs.", e)
    }
  }

  private fun evaluateSolutionAgainstTestCases(solution: Solution, task: Task): List<SolutionTestCase> {
    return task.testCases.map { testCase ->
      val executionResult = executor.executeSingleTestCase(solution.content, testCase.input, testCase.expectedOutput)
      val successOutput = when (executionResult) {
        is ExecutionResult.Success.OutputCorrect -> testCase.expectedOutput
        is ExecutionResult.Success.OutputMismatch -> executionResult.output
        else -> null
      }
      val errorOutput = when (executionResult) {
        is ExecutionResult.Error -> executionResult.message
        else -> null
      }
      SolutionTestCase(successOutput, errorOutput, solution, testCase)
    }
  }
}