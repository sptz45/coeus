/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.http

import org.junit.Test
import org.junit.Assert._
import org.springframework.mock.web.MockHttpServletResponse
import com.coeusweb.mvc.WebResponse

class HttpResponseHeadersTest {
  
  val mock = new MockHttpServletResponse
  val response = new WebResponse(mock)
  
  @Test
  def setting_headers() {
    response.header("Location", "http://google.com")
    assertEquals("http://google.com", mock.getHeader("Location"))
    
    response.header("max-age", 99)
    assertEquals("99", mock.getHeader("max-age"))
    
    val now = new java.util.Date
    response.header("Expires", now)
    assertEquals(now, new java.util.Date((mock.getHeader("Expires").toLong)))
  }
  
  @Test
  def multiple_header_values() {
    response.addHeader("Location", "http://google.com")
    response.addHeader("Location", "http://yahoo.com")
    assertEquals("http://google.com", mock.getHeaders("Location").get(0))
    assertEquals("http://yahoo.com", mock.getHeaders("Location").get(1))
  }

}
