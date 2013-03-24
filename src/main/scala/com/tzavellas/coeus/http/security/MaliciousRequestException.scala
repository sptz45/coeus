/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.http.security

import com.tzavellas.coeus.HttpException
import com.tzavellas.coeus.http.HttpStatus

/**
 * Thrown when the request is believed to be specially crafted to cause a
 * security problem or to leak secret information. 
 */
class MaliciousRequestException(message: String) extends HttpException(message) {

  /** Set to 400 (Bad Request). */
  def httpStatus = HttpStatus.BAD_REQUEST
}