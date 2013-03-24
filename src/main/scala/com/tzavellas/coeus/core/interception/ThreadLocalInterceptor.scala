/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core.interception

import com.tzavellas.coeus.core.RequestContext
import com.tzavellas.coeus.mvc.{ WebRequest, WebResponse }

/**
 * A request interceptor to set {@link WebRequest#currentRequest} and
 * {@link WebResponse#currentResponse}.
 */
class ThreadLocalInterceptor extends Interceptor {

  def preHandle(context: RequestContext) = {
    WebRequest.currentRequest = context.request
    WebResponse.currentResponse = context.response
    true
  }
 
  def postHandle(context: RequestContext) { }
  
  def afterRender(context: RequestContext) {
    WebRequest.currentRequest = null
    WebResponse.currentResponse = null
  }
}
