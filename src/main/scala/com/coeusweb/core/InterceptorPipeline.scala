/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import scala.util.control.ControlThrowable
import scala.collection.mutable.ArrayBuffer
import com.coeusweb.error._
import com.coeusweb.view.{ View, NullView }
import com.coeusweb.interceptor.RequestInterceptor

/**
 * Encapsulates the execution of the specified <code>RequestInterceptor</code>s
 * for a given request.
 * 
 * @param interceptors the interceptors to execute
 * @param exceptionHandler the handler to call if an error occurs
 */
private class InterceptorPipeline(
  interceptors: Iterable[RequestInterceptor],
  exceptionHandler: ExceptionHandler) {

  private[this] val executed = new ArrayBuffer[RequestInterceptor] 
  
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
        case ce: ControlThrowable => throw ce
        case t =>
          context.error = t
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
        case t => if (! context.hasError) {
          context.error = t
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
        case t => if (! context.hasError) context.error = t
      }
    }
  }
}
