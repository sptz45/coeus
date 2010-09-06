/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core
package interception

import org.junit.Test
import com.coeusweb.mvc.WebRequest
import com.coeusweb.test.MockInterceptor

class RequestFilterTest {
  
  val request: RequestContext = new RequestContext(null, null, null)

  @Test
  def filter_chooses_not_to_execute_the_interceptors_for_the_request() {
    val interceptor = new MockInterceptor with RequestFilter {
      def accept(wr: WebRequest) = false
    }
    
    interceptor.preHandle(request)
    interceptor.postHandle(request)
    interceptor.afterRender(request)
    assert(! interceptor.wasCalled, "interceptor was called")
  }
  
  
  @Test
  def filter_chooses_to_execute_the_interceptors_for_the_request() {
    val interceptor = new MockInterceptor with RequestFilter {
      def accept(wr: WebRequest) = true
    }
    
    interceptor.preHandle(request)
    interceptor.postHandle(request)
    interceptor.afterRender(request)
    assert(interceptor.wasCalled, "interceptor did not get called")
  }
}
