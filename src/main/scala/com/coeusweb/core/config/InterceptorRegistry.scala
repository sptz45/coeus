/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.config

import scala.collection.mutable.{ Builder, ListBuffer }
import com.coeusweb.core.interception._

/**
 * A trait to register <code>Interceptor</code> instances for a
 * {@code DispatcherServlet}.
 */
trait InterceptorRegistry {
  
  /**
   * Holds all the registered interceptors.
   * 
   * <p>Interceptors are called during request execution in the order that are
   * registered.</p>
   * 
   * <p>{@link ThreadLocalInterceptor} and {@link FlashScopeInterceptor}
   * are registered by default because basic framework features depend on their
   * function.</p>
   */
  val interceptors: Builder[Interceptor, Seq[Interceptor]] = new ListBuffer
  
  interceptors += new ThreadLocalInterceptor
  interceptors += new FlashScopeInterceptor
}
