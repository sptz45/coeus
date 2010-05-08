/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.interceptor

import com.coeusweb.WebRequest
import com.coeusweb.view.View
import com.coeusweb.core.RequestContext

class ThreadLocalRequestInterceptor extends RequestInterceptor {

  def preHandle(context: RequestContext) = {
    WebRequest.setCurrentRequest(context.request)
    continue
  }
 
  def postHandle(context: RequestContext) { }
  
  
  def afterRender(context: RequestContext) {
    WebRequest.removeCurrentRequest()
  }
}