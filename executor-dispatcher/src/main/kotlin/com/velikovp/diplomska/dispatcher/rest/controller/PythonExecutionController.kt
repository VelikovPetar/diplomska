package com.velikovp.diplomska.dispatcher.rest.controller

import com.velikovp.diplomska.dispatcher.rest.model.ResponseCode
import com.velikovp.diplomska.dispatcher.rest.model.ResponseModel
import com.velikovp.diplomska.dispatcher.rest.model.response.CodeExecutionResponseModel
import com.velikovp.diplomska.dispatcher.rest.model.response.CodeExecutionTestCaseOutcomeModel
import com.velikovp.diplomska.dispatcher.service.SolutionsService
import com.velikovp.diplomska.jwt.JwtTokenParser
import com.velikovp.diplomska.jwt.JwtTokenValidator
import com.velikovp.diplomska.jwt.JwtValidationResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.*

/**
 * Controller handling the requests for execution of python code.
 */
@RestController
class PythonExecutionController(
  val solutionsService: SolutionsService,
  val jwtTokenValidator: JwtTokenValidator,
  val jwtTokenParser: JwtTokenParser
) {

  companion object {
    private const val JWT_TOKEN_HEADER = "token"
    private const val TASK_ID_HEADER = "task-id"
  }

  @Value("\${jwt.token.secret}")
  lateinit var secretKey: String

  private val logger: Logger = LoggerFactory.getLogger(PythonExecutionController::class.java)

  /**
   * Submits a python solution file for execution.
   *
   * @param headers, the request headers.
   * @param file, the solution file.
   *
   * @return [ResponseEntity] holding the result of the execution.
   */
  @RequestMapping("executePython", method = [RequestMethod.POST])
  fun execute(
    @RequestHeader headers: Map<String, String>,
    @RequestParam("file") file: MultipartFile
  ): ResponseEntity<out ResponseModel> {
    val jwtToken = headers[JWT_TOKEN_HEADER]
    jwtToken ?: return ResponseEntity.badRequest()
      .body(ResponseModel(ResponseCode.BAD_REQUEST, "Missing TOKEN header."))
    val taskIdHeader = headers[TASK_ID_HEADER]
    taskIdHeader ?: return ResponseEntity.badRequest()
      .body(ResponseModel(ResponseCode.BAD_REQUEST, "Missing TASK-ID header."))
    val taskId = try {
      taskIdHeader.toLong()
    } catch (e: NumberFormatException) {
      return ResponseEntity.badRequest()
        .body(ResponseModel(ResponseCode.BAD_REQUEST, "Invalid value for TASK-ID header."))
    }

    val tokenVerificationResult = verifyAuthenticationToken(jwtToken)
    logger.debug("Token verification result: $tokenVerificationResult")
    if (tokenVerificationResult !is JwtValidationResult.ResultOk) {
      return ResponseEntity.badRequest().body(ResponseModel(ResponseCode.BAD_REQUEST, "Invalid or missing auth token."))
    }
    val userId = extractUserIdFromJwtToken(jwtToken)
    userId ?: return ResponseEntity.badRequest()
      .body(ResponseModel(ResponseCode.UNKNOWN_ERROR, "An error has occurred during the operation."))
    try {
      val savedSolution = solutionsService.storeSolution(file, "python", taskId, userId)
      val responseBody = CodeExecutionResponseModel(
        taskId,
        savedSolution.solutionTestCases.map {
          CodeExecutionTestCaseOutcomeModel(
            it.testCase.input,
            it.executionSuccessOutput ?: it.executionErrorOutput ?: "",
            it.testCase.expectedOutput
          )
        }
      )
      return ResponseEntity.ok(responseBody)
    } catch (e: Exception) {
      return ResponseEntity.badRequest()
        .body(ResponseModel(ResponseCode.UNKNOWN_ERROR, e.message))
    }
  }

  private fun verifyAuthenticationToken(jwtToken: String): JwtValidationResult {
    val base64encodedSecret = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    return jwtTokenValidator.validateToken(jwtToken, base64encodedSecret)
  }

  private fun extractUserIdFromJwtToken(jwtToken: String): Long? {
    val base64EncodedSecret = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    val claims = jwtTokenParser.parseClaims(jwtToken, base64EncodedSecret)
    val sub = claims["sub"]
    logger.debug("Extracted subject from jwtToken: $sub")
    return sub?.toLongOrNull()
  }
}