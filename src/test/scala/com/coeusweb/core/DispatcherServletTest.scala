/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import javax.servlet.http.HttpServletResponse
import org.junit.{ Test, Before }
import org.junit.Assert._
import org.springframework.mock.web._
import com.coeusweb.mvc.view.NoViewFoundException
import com.coeusweb.test._
import servlet._

class DispatcherServletTest {
  
  val servlet = new DispatcherServlet
  val servletConfig = new MockServletConfig("sweb-test")
  val response = new MockHttpServletResponse
  
  @Before
  def initializeServlet() {
    GlogalState.interceptor.reset()
    servletConfig.addInitParameter(WebModuleLoader.webModuleParamName,
                                   classOf[ExampleWebModule].getName)
    servlet.init(servletConfig)
  }
  
  @Test(expected=classOf[javax.servlet.ServletException])
  def no_module_configured() {
    val emptyConfig = new MockServletConfig("misconfigured")
    val misconfigured = new DispatcherServlet
    misconfigured.init(emptyConfig)
  }
  
  @Test
  def request_and_response_encoding_is_set() {
    val defaultEncoding = "UTF-8"
    val request = req("GET", "/blog/index")
    servlet.service(request, response)
    assertEquals(defaultEncoding, request.getCharacterEncoding)
    assertEquals(defaultEncoding, response.getCharacterEncoding)
    
  }
  
  @Test
  def call_a_controller() {
    servlet.service(req("GET", "/blog/index"), response)
    assertEquals(HttpServletResponse.SC_OK, response.getStatus)
  }
  
  @Test
  def handler_not_found() {
    servlet.service(req("GET", "/notFound"), response)
    assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus)
  }
  
  @Test
  def method_not_allowed() {
    servlet.service(req("DELETE", "/blog/index"), response)
    assertEquals(HttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus)
  }
  
  @Test
  def handler_not_found_if_hide_resources() {
    val no405 = new DispatcherServlet
    val config = new MockServletConfig("sweb-test-with-no-405")
    config.addInitParameter("web-module", classOf[WebModuleWithNo405].getName.toString)
    no405.init(config)
    
    no405.service(req("DELETE", "/blog/index"), response)
    assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus)
  }

  @Test
  def view_instance_get_rendered_correctly() {
    servlet.service(req("GET", "/blog/feed"), response)
    assertEquals("application/atom+xml", response.getContentType)
    assertEquals("<feed></feed>", response.getContentAsString)
  }
  
  @Test
  def view_resolution_test() {
    servlet.service(req("GET", "/blog/entry"), response)
    assertEquals("text/html", response.getContentType)
    assertEquals("Hello World!", response.getContentAsString)
  }
  
  @Test(expected=classOf[NoViewFoundException])
  def exception_when_no_view_found() {
    servlet.service(req("GET", "/blog/noView"), response)
  }
  
  @Test
  def request_interceptor_get_called() {
    servlet.service(req("GET", "/blog/entry"), response)
    assertTrue("interceptor was not called", GlogalState.interceptor.wasCalled)
  }
  
  @Test
  def request_interceptor_does_not_get_called_if_no_handler_found() {
    servlet.service(req("GET", "/doesNotExist"), response)
    assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus)
    assertFalse("interceptor was called", GlogalState.interceptor.wasCalled)
  }
  
  @Test
  def file_uploading() {
    val request =
      (new MultipartRequestBuilder)
        .addFormFile("document", "scala.txt", "hello scala", "text/plain")
        .getRequest("/upload")
    
    servlet.service(request, response)
    val file = GlogalState.uploadedFile
    assertFalse(file.isAvailable) // must have been deleted
    GlogalState.uploadedFile = null
  }

  @Test
  def override_http_method_if_enabled_and_method_param_present() {
    val restful = new DispatcherServlet
    val config = new MockServletConfig("sweb-test-with-http-override")
    config.addInitParameter("web-module", classOf[WebModuleWithOverrideMethod].getName.toString)
    restful.init(config)
    
    val request = req("POST", "/blog/post")
    request.setParameter("_method", "delete")
    restful.service(request, response)
    assertEquals(HttpServletResponse.SC_OK, response.getStatus)
  }
  
  @Test
  def do_not_override_http_method_if_disabled() {
    val request = req("POST", "/blog/post")
    request.setParameter("_method", "delete")
    servlet.service(request, response)
    assertEquals(HttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus)
  }

  def req(method: String, uri: String) = {
    val request = new MockHttpServletRequest
    request.setMethod(method)
    request.setRequestURI(uri)
    request
  }
}
