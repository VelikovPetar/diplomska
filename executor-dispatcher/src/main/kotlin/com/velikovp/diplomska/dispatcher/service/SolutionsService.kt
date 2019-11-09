package com.velikovp.diplomska.dispatcher.service

import com.velikovp.diplomska.dispatcher.database.entity.Solution
import com.velikovp.diplomska.dispatcher.database.entity.SolutionTestCase
import com.velikovp.diplomska.dispatcher.database.entity.SolutionTestCaseKey
import com.velikovp.diplomska.dispatcher.database.repository.SolutionsRepository
import com.velikovp.diplomska.dispatcher.database.repository.TasksRepository
import com.velikovp.diplomska.executor.Executor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

/**
 * Service holding the logic for storing/retrieving [Solution]s from the storage(Database).
 */
@Service
class SolutionsService(@Qualifier("pythonExecutor") private val executor: Executor,
                       private val solutionsRepository: SolutionsRepository,
                       private val tasksRepository: TasksRepository) {

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

    // TODO IMPLEMENT CORRECTLY!
    val filename = StringUtils.cleanPath(
      file.originalFilename ?: throw StorageException("Cannot clean the name of the file to be stored.")
    )
    try {
      if (filename.contains("..")) {
        logger.debug("Filename $filename contains invalid path sequence.")
        throw StorageException("Filename $filename contains invalid path sequence.")
      }
      val task = tasksRepository.findById(taskId).let {
        if (it.isPresent) {
          it.get()
        } else {
          throw StorageException("Cannot retrieve the task for this submission.")
        }
      }

      val solution = Solution(
        filename = filename,
        fileContentType = file.contentType ?: throw StorageException("Cannot retrieve content-type of the file."),
        language = language,
        userId = submittedBy,
        task = task,
        content = file.bytes
      )
      val solutionId = solutionsRepository.save(solution).getId()
      val savedSolution = solutionsRepository.getOne(solutionId!!)
      savedSolution.solutionTestCases = task.testCases.map {
        SolutionTestCase(SolutionTestCaseKey(solutionId, it.getId()!!), /*TODO execute code*/"dummyOutput", savedSolution, it)
      }

      return solutionsRepository.save(savedSolution)
    } catch (e: Exception) {
      logger.debug("Storing of solution file failed. Cause: ${e.message}")
      throw StorageException("Storing of solution file failed. Cause: ${e.message}", e)
    }
  }
}