/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc.scope

import org.junit.Test
import org.junit.Assert._
import com.tzavellas.coeus.mvc.WebRequest
import org.springframework.mock.web.{ MockHttpSession, MockHttpServletRequest }

class FlashScopeTest extends AbstractScopedContainerTest {
  
  val mock = new MockHttpSession
  val servletRequest = new MockHttpServletRequest
  servletRequest.setSession(mock)
  
  val session = new WebSession(mock)
  val attributes = new FlashScope(session)
  
  def setAttributeToMock(name: String, value: Any) {
    FlashScope.getOrCreateFlashMap(session).put(name, new FlashScopeEntry(value))
  }
  
  @Test
  def attributes_get_removed_after_read_from_flash() {
    val creating = createWebRequest
    creating.flash("ten") = 10L
    FlashScope.sweep(creating)
    
    val reading = createWebRequest
    assertEquals(10, reading.flash("ten"))
    FlashScope.sweep(reading)
    
    val evicted = createWebRequest
    assertEquals(None, evicted.flash.get("ten"))
  }
  
  @Test
  def attributes_get_removed_after_read_even_when_created_on_the_same_request() {
    val creating = createWebRequest
    creating.flash("ten") = 10L
    assertEquals(10, creating.flash("ten"))
    FlashScope.sweep(creating)
    
    val evicted = createWebRequest
    assertEquals(None, evicted.flash.get("ten"))
  }
  
  @Test
  def no_object_in_session_if_flash_is_empty() {
    val creating = createWebRequest
    creating.flash("ten") = 10L
    assertEquals(10, creating.flash("ten"))
    FlashScope.sweep(creating)
    
    assertFalse(mock.getAttributeNames.hasMoreElements)
  }
  
  @Test
  def no_race_condition_if_one_request_sweeps_the_flash_while_the_other_is_updating_it() {
    val sweeper = createWebRequest
    sweeper.flash("ten") = 10L
    assertEquals(10, sweeper.flash("ten"))
    
    val mutator = createWebRequest
    
    FlashScope.sweep(sweeper)
    assertFalse("No elements in flash, so session must be empty", mock.getAttributeNames.hasMoreElements)
    
    mutator.flash("mutator") = "hello"
    FlashScope.sweep(mutator)
    assertTrue("Session should contain the flash map", mock.getAttributeNames.hasMoreElements)
  }
  
  
  @Test
  def attribute_persists_multiple_request_if_not_read() {
    val creating = createWebRequest
    creating.flash("ten") = 10L
    FlashScope.sweep(creating)
    
    val intermediate = createWebRequest
    FlashScope.sweep(intermediate)
    
    val reading = createWebRequest
    assertEquals(10, reading.flash("ten"))
    FlashScope.sweep(reading)
    
    val evicted = createWebRequest
    assertEquals(None, evicted.flash.get("ten"))
  }
  
  @Test
  def attributes_get_removed_after_a_time_period_even_if_not_read() {
    val creating = createWebRequest
    creating.flash("ten") = 10L
    FlashScope.sweep(creating)
    
    val expired = createWebRequestWithExpiredFlash
    FlashScope.sweep(expired)
    
    val evicted = createWebRequest
    assertEquals(None, evicted.flash.get("ten"))
  }
  
  def createWebRequest = new WebRequest(null, servletRequest, null, null, null, null)
  
  def createWebRequestWithExpiredFlash =
    new WebRequest(null, servletRequest, null, null, null, null) with ExpiredFlashScope
  
  // static mocks ------------------------------------------------
  
  trait ExpiredFlashScope extends WebRequest {
    val servletRequest: javax.servlet.http.HttpServletRequest
    def session: WebSession
    abstract override lazy val flash = new FlashScope(session) {
      override def hasExpired(entry: FlashScopeEntry) = true
    }
  }
}



