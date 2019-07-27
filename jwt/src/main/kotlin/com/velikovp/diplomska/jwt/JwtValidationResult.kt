package com.velikovp.diplomska.jwt

/**
 * Class representing possible results from the JWT validation.
 */
sealed class JwtValidationResult {
  /**
   * Result representing that the JWT was validated successfully.
   */
  class ResultOk : JwtValidationResult()

  /**
   * Result representing that the JWT is expired.
   */
  class ResultJwtExpired : JwtValidationResult()

  /**
   * Result representing that the JWT is unsupported.
   */
  class ResultJwtUnsupported : JwtValidationResult()

  /**
   * Result representing that the JWT is malformed.
   */
  class ResultJwtMalformed : JwtValidationResult()

  /**
   * Result representing that JWT signature validation failed.
   */
  class ResultJwtInvalidSignature : JwtValidationResult()

  /**
   * Result representing other unexpected error which occurred during the validation.
   */
  class ResultJwtUnknownError : JwtValidationResult()

}