/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb
package mvc

import org.junit.Test
import org.junit.Assert._
import org.springframework.mock.web.MockHttpServletResponse
import test.Assertions.assertThrows
import http.HttpStatus


class WebResponseTest {

  val mock = new MockHttpServletResponse
  val response = new WebResponse(mock)
  
  @Test
  def setting_the_content_type() {
    response.contentType = "application/xml"
    assertEquals("application/xml", mock.getContentType)
    assertEquals(mock.getContentType, response.contentType)
  }
  
  @Test
  def setting_status_code() {
    response.status = HttpStatus.SEE_OTHER
    assertEquals(HttpStatus.SEE_OTHER, mock.getStatus)
  }
  
  @Test
  def setting_error_status() {
    response.sendError(HttpStatus.NOT_FOUND, "404 - Page Not Found")
    assertEquals(HttpStatus.NOT_FOUND, mock.getStatus)
  }
  
  @Test
  def error_accepts_only_error_codes() {
    assertThrows[IllegalArgumentException] { response.sendError(HttpStatus.OK) }
    assertThrows[IllegalArgumentException] { response.sendError(HttpStatus.OK, "message") }
    
    assertThrows[IllegalArgumentException] { response.sendError(HttpStatus.CONTINUE) }
    assertThrows[IllegalArgumentException] { response.sendError(HttpStatus.CONTINUE, "message") }
    
    assertThrows[IllegalArgumentException] { response.sendError(HttpStatus.SEE_OTHER) }
    assertThrows[IllegalArgumentException] { response.sendError(HttpStatus.SEE_OTHER, "message") }
  }
  
  @Test
  def prevent_caching_sets_cache_control_headers() {
    response.preventCaching()
    assertEquals("no-cache, no-store", mock.getHeader("Cache-Control"))
  }
  
  @Test
  def expires_cache_control() {
    response.expires(maxAge=1000)
    assertEquals("max-age=1000", mock.getHeader("Cache-Control"))
    
    response.expires(maxAge=1000, "public", "must-revalidate")
    assertEquals("max-age=1000, public, must-revalidate", mock.getHeader("Cache-Control"))
  }
}
