/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.interception

import com.coeusweb.WebRequest
import com.coeusweb.view.View
import com.coeusweb.core.RequestContext

/**
 * A request interceptor to set {@link WebRequest#currentRequest}.
 */
class ThreadLocalInterceptor extends Interceptor {

  def preHandle(context: RequestContext) = {
    WebRequest.currentRequest = context.request
    continue
  }
 
  def postHandle(context: RequestContext) { }
  
  def afterRender(context: RequestContext) {
    WebRequest.currentRequest = null
  }
}
