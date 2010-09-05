/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.scope

import org.junit.{ Test, Before }
import org.springframework.mock.web.MockServletContext

class ApplicationScopeTest extends AbstractScopedContainerTest with CheckAndActOperationsTest {
  
  val mock = new MockServletContext
  val attributes = new ApplicationScope(mock)
  
  @Before
  def initialize_mutex() {
    ApplicationScope.setupMutex(mock)
  }
  
  def setAttributeToMock(name: String, value: Any) {
    mock.setAttribute(name, value)
  }
  
  @Test
  def needed_for_eclipse_since_concrete_class_has_no_tests() { }
}
