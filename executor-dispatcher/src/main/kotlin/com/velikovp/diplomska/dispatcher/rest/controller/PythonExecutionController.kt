package com.velikovp.diplomska.dispatcher.rest.controller

import com.velikovp.diplomska.dispatcher.rest.model.ResponseCode
import com.velikovp.diplomska.dispatcher.rest.model.ResponseModel
import com.velikovp.diplomska.dispatcher.service.SolutionsService
import com.velikovp.diplomska.dispatcher.service.StorageException
import com.velikovp.diplomska.dispatcher.service.utils.FileUtils
import com.velikovp.diplomska.executor.Executor
import com.velikovp.diplomska.executor.model.ExecutionResult
import com.velikovp.diplomska.jwt.JwtTokenParser
import com.velikovp.diplomska.jwt.JwtTokenValidator
import com.velikovp.diplomska.jwt.JwtValidationResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Paths
import java.util.*

/**
 * Controller handling the requests for execution of python code.
 */
@RestController
class PythonExecutionController(val solutionsService: SolutionsService,
                                @Qualifier("pythonExecutor") private val executor: Executor,
                                // TODO Remove the executor from here, and move the logic to [SolutionsService]
                                val jwtTokenValidator: JwtTokenValidator,
                                val jwtTokenParser: JwtTokenParser) {

  companion object {
    const val JWT_TOKEN_HEADER = "token"
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
  fun execute(@RequestHeader headers: Map<String, String>,
              @RequestParam("file") file: MultipartFile): ResponseEntity<out ResponseModel> {
    val jwtToken = headers[JWT_TOKEN_HEADER]
    jwtToken ?: return ResponseEntity.badRequest().body(ResponseModel(ResponseCode.BAD_REQUEST, "Missing auth token."))

    val tokenVerificationResult = verifyAuthenticationToken(jwtToken)
    logger.debug("Token verification result: $tokenVerificationResult")
    if (tokenVerificationResult !is JwtValidationResult.ResultOk) {
      return ResponseEntity.badRequest().body(ResponseModel(ResponseCode.BAD_REQUEST, "Invalid or missing auth token."))
    }
    val userId = extractUserIdFromJwtToken(jwtToken)
    userId ?: return ResponseEntity.badRequest().body(ResponseModel(ResponseCode.UNKNOWN_ERROR, "An error has occurred during the operation."))
    return try {
      val result = executor.executeSingleTestCase(FileUtils.readContentsFromMultipartFile(file), "3 4", "7")
      when (result) {
        is ExecutionResult.Success.OutputCorrect -> ResponseEntity.ok(ResponseModel())
        is ExecutionResult.Success.OutputMismatch -> ResponseEntity.ok(ResponseModel(errorMessage = "Expected: ${result.expectedOutput}\nActual: ${result.output}"))
        else -> ResponseEntity.badRequest().body(ResponseModel(ResponseCode.BAD_REQUEST, (result as ExecutionResult.Error).message))
      }
    } catch (e: StorageException) {
      logger.debug("Failed to save solution to database.", e)
      ResponseEntity.badRequest().body(ResponseModel(ResponseCode.UNKNOWN_ERROR, "An error has occurred during the operation."))
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