/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import com.coeusweb.http.HttpStatus
/** 
 * The root of exception hierarchy of this framework.
 */
class FrameworkException(message: String = null, cause: Throwable = null)
  extends RuntimeException(message, cause)

/**
 * Thrown during framework initialization when a <code>Controller<code> class has
 * invalid structure.
 * 
 * @see {@link com.coeusweb.Controller Controller}
 * @see ControllerRegistry
 */
class InvalidControllerClassException(message: String) extends FrameworkException(message)

/**
 * A framework exception that also specifies an HTTP status code that the
 * framework should set in the response.
 */
abstract class HttpException(message: String = null, cause: Throwable = null)
  extends FrameworkException(message, cause) {
  
  def httpStatus: Int
}
