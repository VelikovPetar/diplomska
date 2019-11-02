package com.velikovp.diplomska.dispatcher.service

import com.velikovp.diplomska.dispatcher.database.entity.SolutionFile
import com.velikovp.diplomska.dispatcher.database.repository.SolutionFileRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

/**
 * Service holding the logic for storing/retrieving [SolutionFile]s from the storage(Database).
 */
@Service
class SolutionFileStorageService(private val solutionFileRepository: SolutionFileRepository) {

  private val logger: Logger = LoggerFactory.getLogger(SolutionFileStorageService::class.java)

  /**
   * Stores a solution in the storage(database).
   *
   * @param file, the uploaded solution.
   * @param language, the programming language in which the solution is written in.
   * @param taskId, the Id of the task that the solution is submitted for.
   * @param submittedBy, the Id of the user which submitted the solution.
   *
   * @return the [SolutionFile] instance if it was successfully stored.
   * @throws [StorageException] if the storing of the solution fails.
   */
  fun storeFile(file: MultipartFile, language: String, taskId: Long, submittedBy: Long): SolutionFile {
    val filename = StringUtils.cleanPath(
      file.originalFilename ?: throw StorageException("Cannot clean the name of the file to be stored.")
    )
    try {
      if (filename.contains("..")) {
        logger.debug("Filename $filename contains invalid path sequence.")
        throw StorageException("Filename $filename contains invalid path sequence.")
      }
      val solutionFile = SolutionFile(
        filename = filename,
        fileContentType = file.contentType ?: throw StorageException("Cannot retrieve content-type of the file."),
        language = language,
        taskId = taskId,
        submittedBy = submittedBy,
        content = file.bytes
      )
      return solutionFileRepository.save(solutionFile)
    } catch (e: Exception) {
      logger.debug("Storing of solution file failed. Cause: ${e.message}")
      throw StorageException("Storing of solution file failed. Cause: ${e.message}", e)
    }
  }
}