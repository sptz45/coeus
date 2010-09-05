/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb

/** 
 * The root of exception hierarchy of this framework.
 */
class FrameworkException(message: String = null, cause: Throwable = null)
  extends RuntimeException(message, cause)

/**
 * A framework exception that also specifies an HTTP status code that the
 * framework should set in the response.
 */
abstract class HttpException(message: String = null, cause: Throwable = null)
  extends FrameworkException(message, cause) {
  
  /**
   * Suggests the HTTP status of the response that should be used when this
   * exception gets thrown.
   */
  def httpStatus: Int
}
