/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.scope.support

import com.coeusweb.core.RequestContext
import com.coeusweb.interceptor.RequestInterceptor
import com.coeusweb.scope.FlashScope

/**
 * An interceptor the removes stale attributes from <code>FlashScope</code>
 * after every request.
 * 
 * <p>This interceptor in required for the correct behavior of the framework
 * and it is registered by default.</p>
 */
class FlashScopeInterceptor extends RequestInterceptor {
  
  def preHandle(context: RequestContext) = true
  
  def postHandle(context: RequestContext) { }
  
  /**
   * Removes stale attributes from <code>FlashScope</code> by invoking
   * the {@link FlashScope#sweep(WebRequest)} method.
   */
  def afterRender(context: RequestContext) {
    FlashScope.sweep(context.request)
  }
}