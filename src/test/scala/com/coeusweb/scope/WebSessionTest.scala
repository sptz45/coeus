/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.scope

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.test.servlet.MockHttpSession

class WebSessionTest extends AbstractScopedContainerTest with CheckAndActOperationsTest {

  val mock = new MockHttpSession
  val session = new WebSession(mock)
  
  val attributes = session
  
  def setAttributeToMock(name: String, value: Any) {
    mock.setAttribute(name, value)
  }
  
  @Test
  def simple_test_for_isNew() {
    assertTrue(session.isNew)
  }
  
  @Test
  def invalidate_the_http_session() {
    session.invalidate()
    assertTrue(mock.isInvalid)
  }
}
