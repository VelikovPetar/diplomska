package com.velikovp.diplomska.dispatcher.service

import com.velikovp.diplomska.dispatcher.database.entity.Task
import com.velikovp.diplomska.dispatcher.database.entity.TestCase
import com.velikovp.diplomska.dispatcher.database.repository.TasksRepository
import com.velikovp.diplomska.dispatcher.database.repository.TestCasesRepository
import com.velikovp.diplomska.dispatcher.rest.model.request.TestCaseModel
import org.springframework.stereotype.Service

/**
 * Service holding the logic for storing/retrieving [Task]s from the database.
 */
@Service
class TasksService(private val tasksRepository: TasksRepository,
                   private val testCasesRepository: TestCasesRepository) {

  /**
   * Inserts the task into the DB alongside with each test case.
   *
   * @param descriptionText, the description text for the task.
   * @param testCases, the list of test cases.
   */
  fun createTask(descriptionText: String, testCases: List<TestCaseModel>) {
    val task = Task(descriptionText)
    val taskId = tasksRepository.save(task).getId()
    testCasesRepository.saveAll(testCases.map {
      TestCase(task, it.input!!, it.expectedOutput!!)
    })
    if (!tasksRepository.findById(taskId!!).isPresent) {
      throw StorageException("Task cannot be stored.")
    }
  }
}