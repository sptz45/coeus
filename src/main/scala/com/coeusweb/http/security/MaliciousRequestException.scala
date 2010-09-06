/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.http.security

import com.coeusweb.HttpException
import com.coeusweb.http.HttpStatus

/**
 * To be thrown when the request is believed to be specially crafted to cause
 * a security problem or to leak secret information. 
 */
class MaliciousRequestException(message: String) extends HttpException(message) {

  /** Set to 400 (Bad Request). */
  def httpStatus = HttpStatus.BAD_REQUEST
}