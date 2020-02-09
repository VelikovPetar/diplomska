import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

enum class ResponseCode {
  OK,
  BAD_REQUEST,
  EMAIL_EXISTS,
  INVALID_CREDENTIALS,
  UNKNOWN_ERROR
}

data class ResponseModel(
  @field:JsonProperty("responseCode")
  val responseCode: ResponseCode = ResponseCode.OK,
  @field:JsonProperty("errorMessage")
  val errorMessage: String? = null
)

data class Register(
  @field:JsonProperty("name")
  val name: String,
  @field:JsonProperty("surname")
  val surname: String,
  @field:JsonProperty("email")
  val email: String,
  @field:JsonProperty("password")
  val password: String
)

data class Login(
  @field:JsonProperty("email")
  val email: String,
  @field:JsonProperty("password")
  val password: String
)

data class CodeExecutionTestCaseOutcomeModel(
  @field:JsonProperty("input")
  val input: String = "",

  @field:JsonProperty("output")
  val output: String = "",

  @field:JsonProperty("expectedOutput")
  val expectedOutput: String = ""
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CodeExecutionResponseModel(
  @field:JsonProperty("responseCode")
  val responseCode: ResponseCode = ResponseCode.OK,

  @field:JsonProperty("errorMessage")
  val errorMessage: String? = null,

  @field:JsonProperty("taskId")
  val taskId: Long = -1,

  @field:JsonProperty("testCases")
  val testCases: List<CodeExecutionTestCaseOutcomeModel>? = null
)
