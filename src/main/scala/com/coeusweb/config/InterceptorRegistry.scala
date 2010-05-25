/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.config

import com.coeusweb.interceptor._
import com.coeusweb.scope.support.FlashScopeInterceptor

/**
 * A trait to register <code>RequestInterceptor</code> instances for a
 * {@code DispatcherServlet}.
 * 
 * <p>{@link ThreadLocalRequestInterceptor} and {@link FlashScopeInterceptor}
 * are registered by default because basic framework features depend on their
 * function.</p>
 */
trait InterceptorRegistry {
  
  private val _interceptors = Interceptors.newBuilder
  _interceptors += new ThreadLocalRequestInterceptor
  _interceptors += new FlashScopeInterceptor
  _interceptors

  /**
   * Register the specified interceptor.
   * 
   * <p>Interceptors are called during request execution in the order that are
   * registered.</p>
   * 
   * @param interceptor the interceptor to register
   * 
   * @see RequestInterceptor
   * @see com.coeusweb.interceptor.RequestFilter
   */
  def register(interceptor: RequestInterceptor) {
    _interceptors += interceptor
  }
  
  /** A collection with the registered interceptors. */
  def interceptors = new Interceptors(_interceptors)
}