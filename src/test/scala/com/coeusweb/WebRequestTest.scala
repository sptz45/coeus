/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb

import java.util.Locale
import org.junit.Test
import org.junit.Assert._
import bind.ConverterRegistry
import i18n.locale.FixedLocaleResolver
import test.Assertions._
import test.servlet.{MockHttpServletRequest, MockServletContext}

class WebRequestTest extends scope.AbstractScopedContainerTest {

  val mockContext = new MockServletContext
  val mock = new MockHttpServletRequest("GET", "/index")
  
  val request = new WebRequest(servletContext = mockContext,
                               servletRequest = mock,
                               pathContext = null,
                               localeResolver = new FixedLocaleResolver(Locale.US),
                               converters = ConverterRegistry.defaultConverters,
                               messages = null)
  
  val attributes = request
  
  def setAttributeToMock(name: String, value: Any) {
    mock.setAttribute(name, value)
  }
  
  @Test
  def no_sesion_is_created_if_it_does_not_exist() {
    assertEquals(None, request.existingSession)
  }
  
  @Test
  def create_and_get_an_existing_session() {
    val session = request.session
    assertNotNull(session)
    assertSame(session, request.existingSession.get)
  }
  
  @Test
  def reseting_session_creates_a_new_and_invalidates_the_old() {
    val oldSession = request.session
    oldSession("attr") = "some value"
    
    val newSession = request.resetSession()
    
    assertNotSame(oldSession, newSession)
    assertSame(newSession, request.session)
    assertTrue(oldSession.attributes.isEmpty)
  }
  
  @Test
  def getting_the_application_scope_does_not_create_a_session() {
    assertEquals(None, request.existingSession)
    assertNotNull(request.application)
    assertEquals(None, request.existingSession)
  }
  
  @Test
  def get_the_http_method() {
    assertEquals("GET", request.method)
  }
  
  @Test
  def a_request_is_not_ajax_when_X_Requested_With_is_missing() {
    assertFalse(request.isAjax)
  }
  
  @Test
  def a_request_is_not_ajax_when_X_Requested_With_is_not_XMLHttpRequest() {
    mock.addHeader("X-Requested-With", "a browser")
    assertFalse(request.isAjax)
  }
  
  @Test
  def a_request_is_ajax_when_X_Requested_With_is_XMLHttpRequest() {
    mock.addHeader("X-Requested-With", "XMLHttpRequest")
    assertTrue(request.isAjax)
  }
  
  @Test
  def is_modified() {
    assertTrue(request.isModifiedSince(1))
    mock.addHeader("If-Modified-Since", new java.util.Date(0))
    assertTrue(request.isModifiedSince(System.currentTimeMillis))
  }
  
  @Test
  def is_not_modified() {
    mock.addHeader("If-Modified-Since", new java.util.Date(1000))
    assertFalse(request.isModifiedSince(1000))
    assertFalse(request.isModifiedSince(500))
  }
  
  @Test
  def etag_matches() {
    assertFalse(request.etagMatches("12345"))
    mock.addHeader("If-None-Match", "12345")
    assertTrue(request.etagMatches("12345"))
    assertFalse(request.etagMatches(""))
  }
  
  @Test
  def is_multipart_only_when_method_is_post() {
    mock.setContentType("multipart/form-data")
    
    mock.setMethod("POST")
    assertTrue(request.isMultipart)
    
    mock.setMethod("GET")
    assertFalse(request.isMultipart)
    
    mock.setMethod("DELETE")
    assertFalse(request.isMultipart)
  }
  
  @Test
  def is_multipart_when_has_mutipart_content_type() {
    mock.setMethod("POST")
    
    assertFalse(request.isMultipart)
    
    mock.setContentType("multipart/form-data")
    assertTrue(request.isMultipart)
    
    mock.setContentType("multipart/mixed")
    assertTrue(request.isMultipart)
    
    mock.setContentType("application/xml")
    assertFalse(request.isMultipart)
  }
  
  @Test
  def no_files_available_when_no_multipart() {
    assertFalse(request.isMultipart)
    assertNone(request.files("form-file"))
  }
}
