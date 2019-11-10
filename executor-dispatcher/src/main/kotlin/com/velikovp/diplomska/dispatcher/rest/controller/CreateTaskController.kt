package com.velikovp.diplomska.dispatcher.rest.controller

import com.velikovp.diplomska.dispatcher.rest.model.ResponseCode
import com.velikovp.diplomska.dispatcher.rest.model.ResponseModel
import com.velikovp.diplomska.dispatcher.rest.model.request.CreateTaskRequestModel
import com.velikovp.diplomska.dispatcher.rest.model.response.CreateTaskResponseModel
import com.velikovp.diplomska.dispatcher.service.TasksService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * Controller handling the requests for creating new tasks.
 */
@RestController
class CreateTaskController(private val tasksService: TasksService) {

  /**
   * Creates a task with test cases.
   *
   * @param createTaskRequest, the request model with the task description and test cases.
   *
   * @return [ResponseEntity] OK or BAD_REQUEST response.
   */
  @RequestMapping("createTask", method = [RequestMethod.POST])
  fun createTask(@Valid @RequestBody createTaskRequest: CreateTaskRequestModel): ResponseEntity<out ResponseModel> {
    return try {
      val task = tasksService.createTask(createTaskRequest.taskDescription!!, createTaskRequest.testCases!!)
      ResponseEntity.ok(CreateTaskResponseModel(task.getId()!!))
    } catch (e: Exception) {
      ResponseEntity.badRequest()
        .body(ResponseModel(ResponseCode.UNKNOWN_ERROR, e.message))
    }
  }
}