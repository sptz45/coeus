/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.view.helper

import javax.servlet.ServletContext
import org.junit.Test
import org.junit.Assert._
import org.mockito.Mockito._
import com.coeusweb.test.TestHelpers

class UrlHelperTest extends TestHelpers {
  
  val context = mock[ServletContext]
  
  object helper extends UrlHelper {
    val servletContext = context
  }

  @Test
  def url_prepends_context_path() {
    when(context.getContextPath).thenReturn("/test")
    assertEquals("/test/", helper.url("/"))
    assertEquals("/test/page", helper.url("page"))
    assertEquals("/test/page", helper.url("/page"))
  }
  
  @Test
  def url_prepend_context_path_when_context_path_is_root() {
    when(context.getContextPath).thenReturn("")
    assertEquals("/", helper.url("/"))
    assertEquals("/page", helper.url("page"))
    assertEquals("/page", helper.url("/page"))
    
    when(context.getContextPath).thenReturn("/")
    assertEquals("/", helper.url("/"))
    assertEquals("/page", helper.url("page"))
    assertEquals("/page", helper.url("/page"))
  }
  
  @Test
  def url_expects_non_empty_path() {
    assertThrows[IllegalArgumentException] {
      helper.url("")
    }
    assertThrows[IllegalArgumentException] {
      helper.url("", 42)
    }
  }
  
  @Test
  def substitute_vars_in_path() {
    when(context.getContextPath).thenReturn("/test")
    assertEquals("/test/page/42/edit", helper.url("/page/{id}/edit", 42))
  }
}
