package com.velikovp.diplomska.auth.rest.model.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Base model for containing common fields for each response.
 * @param responseCode, the response code
 * @param errorMessage, the exception message if en exception occurred during the execution of the request
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
open class ResponseModel(
  @field:JsonProperty("responseCode") val responseCode: ResponseCode = ResponseCode.OK,
  @field:JsonProperty("errorMessage") val errorMessage: String? = null
)