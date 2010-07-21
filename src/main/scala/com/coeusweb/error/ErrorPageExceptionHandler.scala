/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.error

import com.coeusweb.core.{ RequestContext, HttpException }
import com.coeusweb.view.View

/**
 * The default implementation of <code>ExceptionHandler</code>.
 */
class ErrorPageExceptionHandler(servletName: String) extends ExceptionHandler {
  
  /**
   * Returns <code>ErrorPageView</code> signaling the framework
   * to propagate the exception to the Servlet container.
   * 
   * <p>If the exception that occurred is an instance of
   * <code>HttpException</code> this instance also sets the
   * HTTP status code of the response using the
   * {@link HttpException#httpStatus} field, else it will set
   * the HTTP status to <em>500</em>.</p>
   */
  def handle(context: RequestContext): View = {
    ErrorUtils.setupErrorPageAttributes(context.request, context.error, servletName)
    ErrorUtils.setResposeStatus(context.response, context.error)
    ErrorPageView
  }
}