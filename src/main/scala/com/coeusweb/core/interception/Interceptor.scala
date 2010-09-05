/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.interception

import com.coeusweb.core.RequestContext

/**
 * Intercepts the execution of requests.
 *
 * <p>Instances of this class must be thread-safe.</p>
 *
 * @see {@link com.coeusweb.config.InterceptorRegistry InterceptorRegistry}
 */
trait Interceptor {
  
  /**
   * Called before the request's Handler.
   * 
   * @param context the context of the current request
   * @return true to denote that the request handling should continue,
   *         else false to denote that the request has been handled
   */
  def preHandle(context: RequestContext): Boolean
 
  /**
   * Called after the request's Handler.
   * 
   * @param context the context of the current request
   */
  def postHandle(context: RequestContext)
  
  /**
   * Called after view rendering.
   * 
   * @param context the context of the current request
   */
  def afterRender(context: RequestContext)
  
  /**
   * When returned from the {@link Interceptor#preHandle(context)} method
   * tells the framework to continue with the handling of the request.
   */
  protected def continue = true
  
  /**
   * When returned from the {@link Interceptor#preHandle(context)} method
   * tells the framework that the request has been handled.
   */
  protected def stop = false
}
