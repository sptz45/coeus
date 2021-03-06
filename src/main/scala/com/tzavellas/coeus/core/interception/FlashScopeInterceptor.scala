/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core.interception

import com.tzavellas.coeus.core.RequestContext
import com.tzavellas.coeus.mvc.scope.FlashScope

/**
 * An interceptor that removes stale attributes from <code>FlashScope</code>
 * after every request.
 * 
 * <p>This interceptor in required for the correct behavior of the framework
 * and it is registered by default.</p>
 */
class FlashScopeInterceptor extends Interceptor {
  
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