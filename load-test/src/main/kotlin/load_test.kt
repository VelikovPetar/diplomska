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

const val REGISTER_URL = "http://192.168.39.130:30004/register"
const val LOGIN_URL = "http://192.168.39.130:30004/login"
const val EXECUTE_JAVA_URL = "http://192.168.39.115:31504/executeJava"
const val EXECUTE_PYTHON_URL = "http://192.168.39.115:31504/executePython"

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
  val responseModel = OBJECT_MAPPER.readValue(response.body!!.string(), ResponseModel::class.java)
  if (responseModel.responseCode == ResponseCode.OK) {
    val authToken = response.header("TOKEN", null)
    return Pair(responseModel, authToken)
  }
  return Pair(responseModel, null)
}

fun execute(url: String, taskId: Long, authToken: String, filename: String, solutionFile: File): Pair<CodeExecutionResponseModel?, ResponseModel?> {
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
  val codeExecutionResponseModel = OBJECT_MAPPER.readValue(response.body!!.string(), CodeExecutionResponseModel::class.java)
  return Pair(codeExecutionResponseModel, null)
}

fun createRegistrations(numberOfRegistrations: Int): List<Register> {
  val registrations = mutableListOf<Register>()
  for (i in 1..numberOfRegistrations) {
    registrations.add(Register("name$i", "surname$i", "user$i@mail.com", "password$i"))
  }
  return registrations
}

fun createLogins(numberOfLogins: Int): List<Login> = createRegistrations(numberOfLogins).map {
  Login(it.email, it.password)
}


fun main(args: Array<String>) {
//  println("Hello misiri")
//  createRegistrations(2000).forEach {
//    GlobalScope.launch {
//      val response = register(it)
//      println("Registration of user $it complete with result: ${response.responseCode}/${response.errorMessage}")
//    }
//  }
  createLogins(2000).forEach {
    GlobalScope.launch {
      val pair = loginAndExtractToken(it)
      if (pair.second == null) {
        println("Failed to login user $it with result: ${pair.first.responseCode}/${pair.first.errorMessage}")
      } else {
        println("Login success for user $it with token: ${pair.second}")
      }
    }
  }
//  createLogins(200).forEach {
//    GlobalScope.launch {
//      val loginResponse = loginAndExtractToken(it)
//      if (loginResponse.second != null) {
//        val exResponse = execute(EXECUTE_JAVA_URL, 1118, loginResponse.second!!, "Test.java", File(JAVA_SOLUTION_FILE_PATH))
////        val exResponse = execute(EXECUTE_PYTHON_URL, 1118, loginResponse.second!!, "solution.py", File(PYTHON_SOLUTION_FILE_PATH))
//        println(exResponse)
//      } else {
//        println("Login failed for user $it with code ${loginResponse.first.responseCode} and message ${loginResponse.first.errorMessage}")
//      }
//    }
//  }
  Thread.sleep(2 * 60 * 1_000)
}