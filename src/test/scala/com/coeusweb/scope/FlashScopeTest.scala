/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.scope

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.WebRequest
import com.coeusweb.test.servlet.{ MockHttpSession, MockHttpServletRequest }

class FlashScopeTest extends AbstractScopedContainerTest {
  
  val mock = new MockHttpSession
  val session = new WebSession(mock)
  val attributes = new FlashScope(session)
  
  def setAttributeToMock(name: String, value: Any) {
    FlashScope.getOrCreateFlashMap(session).put(name, new FlashScopeEntry(value))
  }
  
  @Test
  def attributes_get_removed_after_read_from_flash() {
    val servletRequest = new MockHttpServletRequest
    servletRequest.setSession(mock)
    
    val creating = new WebRequest(servletRequest, null, null, null)
    creating.flash("ten") = 10L
    FlashScope.sweep(creating)
    
    val reading = new WebRequest(servletRequest, null, null, null)
    assertEquals(10, reading.flash("ten"))
    FlashScope.sweep(reading)
    
    val evicted = new WebRequest(servletRequest, null, null, null)
    assertEquals(None, evicted.flash.get("ten"))
  }
  
  @Test
  def attributes_get_removed_after_read_even_when_created_on_the_same_request() {
    val servletRequest = new MockHttpServletRequest
    servletRequest.setSession(mock)
    
    val creating = new WebRequest(servletRequest, null, null, null)
    creating.flash("ten") = 10L
    assertEquals(10, creating.flash("ten"))
    FlashScope.sweep(creating)
    
    val evicted = new WebRequest(servletRequest, null, null, null)
    assertEquals(None, evicted.flash.get("ten"))
  }
  
  @Test
  def no_object_in_session_if_flash_is_empty() {
    val servletRequest = new MockHttpServletRequest
    servletRequest.setSession(mock)
    
    val creating = new WebRequest(servletRequest, null, null, null)
    creating.flash("ten") = 10L
    assertEquals(10, creating.flash("ten"))
    FlashScope.sweep(creating)
    
    assertFalse(mock.getAttributeNames.hasMoreElements)
  }
  
  @Test
  def no_race_condition_if_one_request_sweeps_the_flash_while_the_other_is_updating_it() {
    val servletRequest = new MockHttpServletRequest
    servletRequest.setSession(mock)
    
    val sweeper = new WebRequest(servletRequest, null, null, null)
    sweeper.flash("ten") = 10L
    assertEquals(10, sweeper.flash("ten"))
    
    val mutator = new WebRequest(servletRequest, null, null, null)
    
    FlashScope.sweep(sweeper)
    assertFalse("No elements in flash, so session must be empty", mock.getAttributeNames.hasMoreElements)
    
    mutator.flash("mutator") = "hello"
    FlashScope.sweep(mutator)
    assertTrue("Session should contain the flash map", mock.getAttributeNames.hasMoreElements)
  }
  
  
  @Test
  def attribute_persists_multiple_request_if_not_read() {
    val servletRequest = new MockHttpServletRequest
    servletRequest.setSession(mock)
    
    val creating = new WebRequest(servletRequest, null, null, null)
    creating.flash("ten") = 10L
    FlashScope.sweep(creating)
    
    val intermediate = new WebRequest(servletRequest, null, null, null)
    FlashScope.sweep(intermediate)
    
    val reading = new WebRequest(servletRequest, null, null, null)
    assertEquals(10, reading.flash("ten"))
    FlashScope.sweep(reading)
    
    val evicted = new WebRequest(servletRequest, null, null, null)
    assertEquals(None, evicted.flash.get("ten"))
  }
  
  @Test
  def attributes_get_removed_after_a_time_period_even_if_not_read() {
    val servletRequest = new MockHttpServletRequest
    servletRequest.setSession(mock)
    
    val creating = new WebRequest(servletRequest, null, null, null)
    creating.flash("ten") = 10L
    FlashScope.sweep(creating)
    
    val expired = new WebRequest(servletRequest, null, null, null) with ExpiredFlashScope
    FlashScope.sweep(expired)
    
    val evicted = new WebRequest(servletRequest, null, null, null)
    assertEquals(None, evicted.flash.get("ten"))
  }
  
  
  // static mocks ------------------------------------------------
  
  trait ExpiredFlashScope extends WebRequest {
    val servletRequest: javax.servlet.http.HttpServletRequest
    def session: WebSession
    abstract override lazy val flash = new FlashScope(session) {
      override def hasExpired(entry: FlashScopeEntry) = true
    }
  }
}



