/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.http.security

import com.coeusweb.interceptor.RequestInterceptor
import com.coeusweb.core.RequestContext
import com.coeusweb.view.View

/**
 * Protects from CSRF attacks.
 * 
 * @see CsrfProtection
 */
class CsrfInterceptor extends RequestInterceptor {

  def preHandle(context: RequestContext) = {
    CsrfProtection.assertOrigin(context.request)
    continue
  }
 
  def postHandle(context: RequestContext) { }
  
  def afterRender(context: RequestContext) { }
}