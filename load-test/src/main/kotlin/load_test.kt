import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.lang.Exception

val HTTP_CLIENT = OkHttpClient()
val OBJECT_MAPPER = ObjectMapper()
val JSON = "application/json; charset=UTF-8".toMediaType()
val MUTLI_PART = "multipart/form-data".toMediaType()

const val REGISTER_URL = "http://192.168.39.172:32299/register"
const val LOGIN_URL = "http://192.168.39.172:32299/login"
const val EXECUTE_JAVA_URL = "http://192.168.39.172:30012/executeJava"
const val EXECUTE_PYTHON_URL = "http://192.168.39.172:30012/executePython"

const val PYTHON_SOLUTION_FILE_PATH = "/home/petarv/Documents/diplomska/load-test/src/main/kotlin/solution.py"
const val JAVA_SOLUTION_FILE_PATH = "/home/petarv/Documents/diplomska/load-test/src/main/kotlin/Test.java"

fun <T, R> post(url: String, body: T, responseClass: Class<R>): R {
  val requestBody = OBJECT_MAPPER.writeValueAsString(body).toRequestBody(JSON)
  val request = Request.Builder().apply {
    url(url)
    post(requestBody)
  }.build()
  val response = try {
    HTTP_CLIENT.newCall(request).execute()
  } catch (e: Exception) {
    println("Failed to execute request for $body on url: $url")
    return ResponseModel(ResponseCode.UNKNOWN_ERROR, "Exception.") as R
  }
  return OBJECT_MAPPER.readValue(response.body!!.string(), responseClass)
}

fun register(register: Register): ResponseModel {
  return post(REGISTER_URL, register, ResponseModel::class.java)
}

fun loginAndExtractToken(login: Login): Pair<ResponseModel, String?> {
  val requestBody = OBJECT_MAPPER.writeValueAsString(login).toRequestBody(JSON)
  val request = Request.Builder().apply {
    url(LOGIN_URL)
    post(requestBody)
  }.build()
  val response = try {
    HTTP_CLIENT.newCall(request).execute()
  } catch (e: Exception) {
    println("Failed to execute request for $login on url: $LOGIN_URL")
    return Pair(
      ResponseModel(ResponseCode.UNKNOWN_ERROR, "Failed to execute request for $login on url: $LOGIN_URL"),
      null
    )
  }
  val responseModel = try {
    OBJECT_MAPPER.readValue(response.body!!.string(), ResponseModel::class.java)
  } catch (e: Exception) {
    return Pair(
      ResponseModel(ResponseCode.UNKNOWN_ERROR, "Failed to execute request for $login on url: $LOGIN_URL"),
      null
    )
  }
  if (responseModel.responseCode == ResponseCode.OK) {
    val authToken = response.header("TOKEN", null)
    return Pair(responseModel, authToken)
  }
  return Pair(responseModel, null)
}

fun execute(
  url: String,
  taskId: Long,
  authToken: String,
  filename: String,
  solutionFile: File
): Pair<CodeExecutionResponseModel?, ResponseModel?> {
  val requestBody = MultipartBody.Builder()
    .addFormDataPart("file", filename, solutionFile.asRequestBody("multipart/form-data".toMediaType()))
    .build()
  val request = Request.Builder().apply {
    url(url)
    post(requestBody)
    header("TOKEN", authToken)
    header("TASK-ID", taskId.toString())
  }.build()
  val response = try {
    HTTP_CLIENT.newCall(request).execute()
  } catch (e: Exception) {
    return Pair(null, ResponseModel(ResponseCode.UNKNOWN_ERROR, "Failed to execute call on url: $url."))
  }
  val codeExecutionResponseModel =
    OBJECT_MAPPER.readValue(response.body!!.string(), CodeExecutionResponseModel::class.java)
  return Pair(codeExecutionResponseModel, null)
}

fun createRegistrations(numberOfRegistrations: Int): List<Register> {
  val registrations = mutableListOf<Register>()
  for (i in 1..numberOfRegistrations) {
    registrations.add(Register("name$i-1", "surname$i-1", "user$i@mail.com-1", "password$i-1"))
  }
  return registrations
}

fun createLogins(numberOfLogins: Int): List<Login> = createRegistrations(numberOfLogins).map {
  Login(it.email, it.password)
}

open class App {

  companion object {

    private enum class Action(val value: String) {
      REGISTRATION("reg"),
      AUTHENTICATION("auth"),
      EVALUATION("eval")
    }

    private fun performRegistrations(n: Int) {
      println("Performing $n registrations...")
      createRegistrations(n).forEach { reg ->
        GlobalScope.launch {
          val response = register(reg)
          println("Registration of user $reg complete with result: ${response.responseCode}/${response.errorMessage}")
        }
      }
      Thread.sleep(2 * 60 * 1000)
    }

    private fun performLogins(n: Int, duration: Int) {
      for (i in 0..duration) {
        println("[Batch $i] Executing $n login requests.")
        createLogins(n).forEach { login ->
          GlobalScope.launch {
            val loginResult = loginAndExtractToken(login)
            if (loginResult.second == null) {
              println("Failed to login user $login with result: ${loginResult.first.responseCode}/${loginResult.first.errorMessage}")
            } else {
              println("Login success for user $login with token: ${loginResult.second}")
            }
          }
        }
        Thread.sleep(1_000)
      }
      Thread.sleep(2 * 60 * 1000)
    }

    private fun performEvaluations(n: Int, duration: Int) {
      for (i in 0..duration) {
        createLogins(n).forEach { login ->
          GlobalScope.launch {
            val loginResult = loginAndExtractToken(login)
            if (loginResult.second == null) {
              val exResponse = execute(EXECUTE_JAVA_URL, 1118, loginResult.second!!, "Test.java", File(JAVA_SOLUTION_FILE_PATH))
              println(exResponse)
            } else {
              println("Login success for user $login with token: ${loginResult.second}")
            }
          }
        }
        Thread.sleep(1_000)
      }
      Thread.sleep(2 * 60 * 1000)
    }

    private fun handleRegistration(args: Array<String>) {
      if (args.size < 2) {
        println("No number of requested registrations entered.")
        return
      }
      val n = args[1].toIntOrNull()
      if (n == null) {
        println("Invalid number of requested registrations entered.")
        return
      }
      performRegistrations(n)
    }

    private fun handleAuthentication(args: Array<String>) {
      if (args.size < 2) {
        println("No number of requested logins/second entered.")
        return
      }
      val n = args[1].toIntOrNull()
      if (n == null) {
        println("Invalid number of requested logins/second entered.")
        return
      }
      if (args.size < 3) {
        println("No duration entered (seconds) entered.")
        return
      }
      val duration = args[2].toIntOrNull()
      if (duration == null) {
        println("No valid duration entered (seconds) entered.")
        return
      }
      performLogins(n, duration)
    }

    private fun handleEvaluation(args: Array<String>) {
      if (args.size < 2) {
        println("No number of requested logins/second entered.")
        return
      }
      val n = args[1].toIntOrNull()
      if (n == null) {
        println("Invalid number of requested logins/second entered.")
        return
      }
      if (args.size < 3) {
        println("No duration entered (seconds) entered.")
        return
      }
      val duration = args[2].toIntOrNull()
      if (duration == null) {
        println("No valid duration entered (seconds) entered.")
        return
      }
      performEvaluations(n, duration)
    }

    @JvmStatic
    fun main(args: Array<String>) {
      if (args.isEmpty()) {
        println("No action provided. Please provide one of the following: [${Action.values().joinToString(separator = ", ") { it.value }}]")
        return
      }
      when (args[0]) {
        Action.REGISTRATION.value -> handleRegistration(args)
        Action.AUTHENTICATION.value -> handleAuthentication(args)
        Action.EVALUATION.value -> handleEvaluation(args)
        else -> {
          println("Invalid action provided. Please provide one of the following: [${Action.values().joinToString(separator = ", ") { it.value }}]")
        }
      }
    }
  }
}