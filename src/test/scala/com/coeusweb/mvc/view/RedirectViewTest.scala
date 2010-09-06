/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.view

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.mvc.{ WebRequest, WebResponse }
import com.coeusweb.mvc.controller.AbstractController
import org.springframework.mock.web.{ MockHttpServletRequest, MockHttpServletResponse }
import com.coeusweb.http.HttpStatus

class RedirectViewTest {

  val mockRequest = new MockHttpServletRequest
  val mock = new MockHttpServletResponse
  val request = new WebRequest(null, mockRequest, null, null, null, null)
  val response = new WebResponse(mock)
  val controller = new AbstractController { }
  controller.response = response
  
  @Test
  def redirect_with_default_code() {
    val view = controller.redirect("/blog")
    view.render(request, response)
    assertEquals(HttpStatus.SEE_OTHER, mock.getStatus)
    assertEquals("/blog", mock.getHeader("Location"))
  }
  
  @Test
  def redirect_with_specified_code() {
    val view = controller.redirect("/blog", HttpStatus.MOVED_PERMANENTLY)
    view.render(request, response)
    assertEquals(HttpStatus.MOVED_PERMANENTLY, mock.getStatus)
    assertEquals("/blog", mock.getHeader("Location"))
  }
  
  @Test(expected=classOf[IllegalArgumentException])
  def redirect_with_code_not_in_redirection_series() {
    val view = controller.redirect("/blog", HttpStatus.OK)
    view.render(request, response)
  }
  
  @Test
  def redirect_prepends_context_path() {
    mockRequest.setContextPath("/coeusblog")
    controller.redirect("/blog").render(request, response)
    assertEquals("/coeusblog/blog", mock.getHeader("Location"))
  }
  
  @Test
  def redirect_to_external_url() {
    controller.redirect("http://google.com").render(request, response)
    assertEquals("http://google.com", mock.getHeader("Location"))
    controller.redirect("https://google.com").render(request, response)
    assertEquals("https://google.com", mock.getHeader("Location"))
  }
  
  @Test
  def redirect_with_parameters() {
    val view = controller.redirect("/blog", "error" -> true)
    view.render(request, response)
    assertEquals("/blog?error=true", mock.getHeader("Location"))
  }
  
  @Test
  def redirect_with_parameters_using_location_with_existing_parameters() {
    val view = controller.redirect("/blog?id=42", "error" -> true)
    view.render(request, response)
    assertEquals("/blog?id=42&error=true", mock.getHeader("Location"))
  }
}
