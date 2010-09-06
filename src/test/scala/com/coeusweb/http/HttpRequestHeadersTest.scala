/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.http

import java.util.{ Date, Locale }
import java.net.URI
import org.junit.Test
import org.junit.Assert._
import org.springframework.mock.web.MockHttpServletRequest
import com.coeusweb.bind.ConverterRegistry
import com.coeusweb.i18n.locale.FixedLocaleResolver
import com.coeusweb.mvc.WebRequest
import com.coeusweb.test.Assertions._

class HttpRequestHeadersTest {

  val mock = new MockHttpServletRequest
  val request = new WebRequest(null, mock, null,
                               new FixedLocaleResolver(Locale.US),
                               ConverterRegistry.defaultConverters, null)
  
  @Test
  def read_string_headers_does_not_require_type_argument() {
    mock.addHeader("Location", "here")
    assertSome("here", request.header("Location"))
  }
  
  @Test
  def read_int_header() {
    mock.addHeader("max-age", "99")
    assertSome(99, request.headerInt("max-age"))
  }
  
  @Test
  def read_date_header() {
    val date = new Date
    mock.addHeader("date", date)
    assertSome(date.getTime, request.headerDate("date"))
  }
  
  @Test
  def read_non_existing_header() {
    assertNone(request.headerInt("does not exist"))
    assertNone(request.header("does not exist"))
    assertNone(request.headerDate("does not exist"))
  }
  
  @Test
  def header_names() {
    assertFalse(request.headerNames.hasNext)
    mock.addHeader("Location", "here")
    assertTrue(request.headerNames.contains("Location"))
  }
  
  @Test
  def multiple_header_values() {
    assertFalse(request.headerValues("does-not-exist").hasNext)
    mock.addHeader("accept-encoding", "gzip")
    mock.addHeader("accept-encoding", "deflate")
    assertTrue(request.headerValues("accept-encoding").contains("gzip"))
    assertTrue(request.headerValues("accept-encoding").contains("deflate"))
  }
}
