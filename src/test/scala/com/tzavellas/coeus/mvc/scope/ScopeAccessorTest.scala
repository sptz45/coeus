/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc
package scope

import org.junit.Test
import org.junit.Assert._
import org.springframework.mock.web.MockHttpServletRequest

class ScopeAccessorTest {
  
  val response = new WebResponse(null)
  val request = new WebRequest(new ApplicationScope(null), new MockHttpServletRequest,
                               null, null, null, null)
  
  @Test
  def access_the_various_scopes() {
    val scopes = ScopeAccessor(request, response)
    assertNotNull(scopes.request)
    assertNotNull(scopes.response)
    assertNotNull(scopes.application)
    assertNotNull(scopes.session)
    assertNotNull(scopes.flash)
  }

}