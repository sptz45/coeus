/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb
package http

import java.util.{ Date, Locale }
import java.net.URI
import org.junit.Test
import org.junit.Assert._
import com.coeusweb.bind.DefaultConverterRegistry
import com.coeusweb.i18n.locale.FixedLocaleResolver
import test.servlet.MockHttpServletRequest
import test.Assertions._

class HttpRequestHeadersTest {

  val mock = new MockHttpServletRequest
  val request = new WebRequest(mock, null, new FixedLocaleResolver(Locale.US), DefaultConverterRegistry)
  
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
  def parse_header_value() {
    val google = "http://google.com"
    mock.addHeader("Location", google)
    assertSome(new URI(google), request.headerParse[URI]("Location"))
  }
  
  @Test
  def read_non_existing_header() {
    assertNone(request.headerInt("does not exist"))
    assertNone(request.header("does not exist"))
    assertNone(request.headerDate("does not exist"))
    assertNone(request.headerParse[URI]("does not exist"))
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
