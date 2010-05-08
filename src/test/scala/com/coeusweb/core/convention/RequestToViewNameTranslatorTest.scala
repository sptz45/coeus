/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.convention

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.WebRequest
import com.coeusweb.test.servlet.MockHttpServletRequest

class RequestToViewNameTranslatorTest {
  import RequestToViewNameTranslator.viewNameForRequest
  
  @Test
  def use_the_request_uri() {
    assertEquals("index", viewNameForRequest(request("/index")))
    assertEquals("blog/entries/test", viewNameForRequest(request("/blog/entries/test/")))
  }
  
  @Test
  def return_index_as_a_view_for_root() {
    assertEquals("index", viewNameForRequest(request("/")))
  }
  
  @Test
  def file_extension_get_droped_from_the_view_name() {
    assertEquals("index",      viewNameForRequest(request("/index.html")))
    assertEquals("index.html", viewNameForRequest(request("/index.html/")))
    assertEquals("file.index", viewNameForRequest(request("/file.index.html")))
  }
  
  def request(uri: String) =
    new WebRequest(new MockHttpServletRequest("GET", uri), null, null, null)
}