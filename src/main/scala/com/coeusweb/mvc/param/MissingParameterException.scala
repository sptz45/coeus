/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.param

import com.coeusweb.{HttpException, FrameworkException}
import com.coeusweb.http.HttpStatus

/**
 * An exception to be thrown when user tries to retrieve a parameter (path or request)
 * that does not exist.
 *
 * @param parameter the name of the parameter that does not exist.
 */
class MissingParameterException(parameter: String)
  extends HttpException("Could not find parameter: '"+parameter+"'") {
  
  def httpStatus = HttpStatus.BAD_REQUEST
}