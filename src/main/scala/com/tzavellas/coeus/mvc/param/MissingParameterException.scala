/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc.param

import com.tzavellas.coeus.HttpException
import com.tzavellas.coeus.http.HttpStatus

/**
 * An exception to be thrown when user tries to retrieve a parameter that does
 * not exist.
 *
 * @param parameter the name of the parameter that does not exist.
 */
class MissingParameterException(parameter: String)
  extends HttpException("Could not find parameter: '"+parameter+"'") {
  
  /** Set to 400 (Bad Request). */
  def httpStatus = HttpStatus.BAD_REQUEST
}