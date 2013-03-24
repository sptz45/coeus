/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.http.multipart

import javax.servlet.{ ServletContext, ServletContextEvent }
import javax.servlet.http.HttpServletRequest

/**
 * A {@code MultipartRequestParser} that throws {@code UnsupportedOperationException}
 * when asked to parse a request.
 * 
 * @see CommonsMultipartRequestParser
 */
class NullMultipartRequestParser extends MultipartRequestParser {

  def init(context: ServletContext) { }
  def destroy(context: ServletContext) { }
  def cleanupFiles(request: MultipartHttpServletRequest) { }

  /** Throws {@code UnsupportedOperationException}. */
  def parse(request: HttpServletRequest): MultipartHttpServletRequest = {
    throw new UnsupportedOperationException(
      "Multipart requests are not supported in the default configuration. " +
      "Please configure a MultipartRequestParser in DispatcherConfig.")
  }
}
