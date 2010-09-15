/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import javax.servlet.http.HttpServletResponse

private object HttpOptionsResponseGenerator {
  
  /**
   * Generates an response for an HTTP OPTIONS request using the specified
   * allowed methods.
   */
  def generate(res: HttpServletResponse, handlers: HandlerMap, allowHttpHead: Boolean) {
    
    var methods = handlers.supportedMethods
    if (!methods.contains("HEAD") && allowHttpHead)
      methods = methods + "HEAD"

    res.setStatus(HttpServletResponse.SC_OK)
    res.setHeader("Allow", methods.mkString(", "))
    res.setHeader("Cache-Control", "no-cache, no-store")
    res.setHeader("Content-Length", "0")
    res.flushBuffer()
  }
}