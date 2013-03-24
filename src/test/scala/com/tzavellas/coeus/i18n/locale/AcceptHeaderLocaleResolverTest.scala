/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.i18n.locale

import java.util.Locale
import org.junit.Test
import org.junit.Assert._
import org.springframework.mock.web.MockHttpServletRequest

class AcceptHeaderLocaleResolverTest {

  val resolver = new AcceptHeaderLocaleResolver
  val request = new MockHttpServletRequest
  
  @Test
  def retrieve_locale_from_the_request() {
    request.addPreferredLocale(Locale.UK)
    assertEquals(Locale.UK, resolver.resolve(request))
    
    request.addPreferredLocale(Locale.US)
    assertEquals(Locale.US, resolver.resolve(request))
  }
  
  @Test(expected=classOf[UnsupportedOperationException])
  def cannot_set_the_locale_explicitly() {
    resolver.setLocale(null, null, Locale.US)
  }
}
