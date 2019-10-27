package com.velikovp.diplomska.dispatcher.rest.model

/**
 * FIXME: Unify this model with the one from the :auth module in a separate module.
 */
enum class ResponseCode {
  OK,
  BAD_REQUEST,
  EMAIL_EXISTS,
  INVALID_CREDENTIALS,
  UNKNOWN_ERROR
}