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
  def no_module_configured_for_servlet_in_web_xml() {
    val emptyConfig = new MockServletConfig("misconfigured")
    val misconfigured = new DispatcherServlet
    misconfigured.init(emptyConfig)
  }
  
  @Test
  def request_and_response_encoding_is_set_as_configured_in_dispatcher_config() {
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
  def status_is_404_if_a_handler_is_not_found_for_a_url() {
    servlet.service(req("GET", "/notFound"), response)
    assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus)
  }
  
  @Test
  def status_is_405_if_handlers_exists_for_the_url_but_do_not_support_the_requested_http_mehtod() {
    servlet.service(req("DELETE", "/blog/index"), response)
    assertEquals(HttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus)
  }
  
  @Test
  def status_is_404_if_method_not_allowed_but_hide_resources_is_set_in_dispatcher_config() {
    setWebModuleTo[WebModuleWithNo405]

    servlet.service(req("DELETE", "/blog/index"), response)
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
    setWebModuleTo[WebModuleWithOverrideMethod]

    val request = req("POST", "/blog/post")
    request.setParameter("_method", "delete")
    servlet.service(request, response)
    assertEquals(HttpServletResponse.SC_OK, response.getStatus)
  }
  
  @Test
  def do_not_override_http_method_if_disabled() {
    val request = req("POST", "/blog/post")
    request.setParameter("_method", "delete")
    servlet.service(request, response)
    assertEquals(HttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus)
  }

  @Test
  def the_default_handling_of_head_and_options_http_methods_is_not_enabled_by_default() {
    val get = req("GET", "/blog/feed")
    servlet.service(get, response)
    assertEquals(HttpServletResponse.SC_OK, response.getStatus)

    var _405 = new MockHttpServletResponse
    val head = req("HEAD", "/blog/feed")
    servlet.service(head,_405)
    assertEquals(HttpServletResponse.SC_METHOD_NOT_ALLOWED, _405.getStatus)

    _405 = new MockHttpServletResponse
    val options = req("OPTIONS", "/blog/feed")
    servlet.service(options, _405)
    assertEquals(HttpServletResponse.SC_METHOD_NOT_ALLOWED, _405.getStatus)
  }
  
  @Test
  def respond_to_head_for_url_that_support_get_if_enabled() {
    setWebModuleTo[WebModuleWithMethodsEnabled]

    val request = req("HEAD", "/blog/feed")
    servlet.service(request, response)
    assertEquals(HttpServletResponse.SC_OK, response.getStatus)
  }
  
  @Test
  def handler_method_annotated_with_head_gets_executed_instead_of_default_head_behavior() {
    setWebModuleTo[WebModuleWithMethodsEnabled]

    val request = req("HEAD", "/blog/entry")
    servlet.service(request, response)
    assertEquals(402, response.getStatus)
  }
  
  @Test
  def respond_to_options_for_an_existing_url_if_enabled() {
    setWebModuleTo[WebModuleWithMethodsEnabled]

    val request = req("OPTIONS", "/blog/feed")
    servlet.service(request, response)
    assertEquals(HttpServletResponse.SC_OK, response.getStatus)
    val allow = response.getHeader("Allow").asInstanceOf[String]
    assertTrue(allow.contains("GET"))
    assertTrue(allow.contains("HEAD"))
  }
  
  @Test
  def status_is_404_if_options_requested_in_a_nonexisting_resource() {
    setWebModuleTo[WebModuleWithMethodsEnabled]

    val request = req("OPTIONS", "/blog/does-not-exist")
    servlet.service(request, response)
    assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus)
  }
  
  @Test
  def handler_method_annotated_with_options_gets_executed_instead_of_default_options_behavior() {
    setWebModuleTo[WebModuleWithMethodsEnabled]

    val request = req("OPTIONS", "/blog/entry")
    servlet.service(request, response)
    assertEquals(402, response.getStatus)
  }

  def setWebModuleTo[T: Manifest] = {
    val config = new MockServletConfig("test-servlet")
    config.addInitParameter("web-module", implicitly[Manifest[T]].erasure.getName.toString)
    servlet.init(config)
  }
  
  def req(method: String, uri: String) = {
    val request = new MockHttpServletRequest
    request.setMethod(method)
    request.setRequestURI(uri)
    request
  }
}
