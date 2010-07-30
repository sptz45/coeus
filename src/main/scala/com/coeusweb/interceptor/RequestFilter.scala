/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb
package interceptor

import view.View
import core.RequestContext

/**
 * A stackable trait to filter the requests that a {@code Interceptor}
 * intercepts. 
 */
trait RequestFilter extends Interceptor {
  
  /**
   * Decide if the <code>Interceptor</code> is going to intercept
   * the given request.
   * 
   * @param request the current request
   */
  def accept(request: WebRequest): Boolean
  
  abstract override def preHandle(context: RequestContext): Boolean = {
    if (accept(context.request)) super.preHandle(context)
    else continue
  }
  
  abstract override def postHandle(context: RequestContext) {
    if (accept(context.request))
      super.postHandle(context)
  }
  
  abstract override def afterRender(context: RequestContext) {
    if (accept(context.request))
      super.afterRender(context)
  }
}
