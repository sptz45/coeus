/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.util

import org.junit.Test
import org.junit.Assert._
import org.springframework.mock.web.MockHttpServletRequest
import com.coeusweb.WebRequest

class ConventionsTest {
  import Conventions._

  @Test
  def the_attribute_name_is_the_simple_class_name_with_the_first_char_to_lower_case() {
    assertEquals("string", classToAttributeName(classOf[String]))
    assertEquals("threadLocal", classToAttributeName(classOf[ThreadLocal[_]]))
    assertEquals("illegalStateException", classToAttributeName(classOf[IllegalStateException]))
  }
  
  @Test
  def the_view_name_is_the_request_uri() {
    assertEquals("index", viewNameForRequest(request("/index")))
    assertEquals("blog/entries/test", viewNameForRequest(request("/blog/entries/test/")))
  }
  
  @Test
  def the_view_name_of_root_uri_is_index() {
    assertEquals("index", viewNameForRequest(request("/")))
  }
  
  @Test
  def the_view_name_does_not_contain_file_extensions() {
    assertEquals("index",      viewNameForRequest(request("/index.html")))
    assertEquals("index.html", viewNameForRequest(request("/index.html/")))
    assertEquals("file.index", viewNameForRequest(request("/file.index.html")))
  }
  
  def request(uri: String) =
    new WebRequest(null, new MockHttpServletRequest("GET", uri), null, null, null, null)
}