/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core

import com.tzavellas.coeus.mvc.{ WebRequest, WebResponse }

/**
 * Holds information about the current request.
 * 
 * @param request  the current request
 * @param response the current response
 * @param handler  the handler that handles the request
 */
final class RequestContext(val request: WebRequest, val response: WebResponse, val handler: Handler) {

  /**
   * The exception that occurred during the handling of the request.  
   */
  var error: Exception = _

  /**
   * The result of handling the request.
   * 
   * <p>It can be a <code>View</code> instance, a String that can be mapped to
   *  a view, a <code>NodeSeq</code> or <code>null</code>.</p>
   */
  var result: Any = _
  
  /**
   * Returns true if an uncaught exception has occurred during the
   * handling of the request.
   */
  def hasError = error ne null
}
