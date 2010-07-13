/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.config

import scala.collection.mutable.{ Builder, ListBuffer }
import com.coeusweb.interceptor._
import com.coeusweb.scope.support.FlashScopeInterceptor

/**
 * A trait to register <code>RequestInterceptor</code> instances for a
 * {@code DispatcherServlet}.
 */
trait InterceptorRegistry {
  
  /**
   * Holds all the registered interceptors.
   * 
   * <p>Interceptors are called during request execution in the order that are
   * registered.</p>
   * 
   * <p>{@link ThreadLocalRequestInterceptor} and {@link FlashScopeInterceptor}
   * are registered by default because basic framework features depend on their
   * function.</p>
   */
  val interceptors: Builder[RequestInterceptor, Seq[RequestInterceptor]] = new ListBuffer
  
  interceptors += new ThreadLocalRequestInterceptor
  interceptors += new FlashScopeInterceptor
}
