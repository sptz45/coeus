/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import javax.servlet.http.HttpServletResponse
import scala.collection.Set

private object HttpResponseGenerator {
  
  /**
   * Writes an response for an HTTP OPTIONS request using the specified
   * allowed methods.
   */
  def writeOptions(response: HttpServletResponse, methods: Set[String]) {
    response.setStatus(HttpServletResponse.SC_OK)
    response.setHeader("Allow", methods.mkString(", "))
    response.setHeader("Cache-Control", "no-cache, no-store")
    response.setHeader("Content-Length", "0")
    response.flushBuffer()
  }
  
  /**
   * Writes a response for <em>405 Method Not Allowed</em> that includes
   * an <em>Allow</em> header with the allowed methods.
   */
  def writeMethodNotAllowed(response: HttpServletResponse, methods: Set[String]) {
    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
    response.setHeader("Allow", methods.mkString(", "))
    response.setHeader("Content-Length", "0")
    response.flushBuffer()
  }
}