/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import scala.collection.mutable.ArrayBuffer
import com.coeusweb.mvc.view.NullView
import interception.Interceptor
import error.ExceptionHandler

/**
 * Encapsulates the execution of the specified <code>Interceptor</code>s
 * for a given request.
 * 
 * @param interceptors the interceptors to execute
 * @param exceptionHandler the handler to call if an error occurs
 */
private class InterceptorPipeline(
  interceptors: Iterable[Interceptor],
  exceptionHandler: ExceptionHandler) {

  private[this] val executed = new ArrayBuffer[Interceptor] 
  
  def executePreHandle(context: RequestContext): Boolean = {
    for (interceptor <- interceptors) {
      executed += interceptor
      try {
       val continue = interceptor.preHandle(context)
       if (! continue) {
         context.result = NullView
         return false
       }
         
      } catch {
        case e: Exception =>
          context.error = e
          context.result = exceptionHandler.handle(context)
          return false
      }
    }
    true
  }
  
  def executePostHandle(context: RequestContext) {
    for (interceptor <- executed) {
      try {
        interceptor.postHandle(context)   
      } catch {
        case e: Exception =>
          if (! context.hasError) {
            context.error = e
            context.result = exceptionHandler.handle(context)
        }
      }
    }
  }
  
  def executeAfterRender(context: RequestContext) {
    for (interceptor <- executed) {
      try {
        interceptor.afterRender(context)
      } catch {
        case e: Exception =>
        if (! context.hasError)
          context.error = e
      }
    }
  }
}
