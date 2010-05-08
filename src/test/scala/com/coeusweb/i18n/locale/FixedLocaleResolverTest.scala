/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.i18n.locale

import java.util.Locale
import org.junit.Test
import org.junit.Assert._

class FixedLocaleResolverTest {
  
  val request, response = null
  val defaultLocale = Locale.UK
  val resolver = new FixedLocaleResolver(defaultLocale)

  @Test
  def return_the_default_if_a_locale_is_not_set() {
    assertEquals(defaultLocale, resolver.resolve(request))
  }
  
  @Test
  def setting_locale() {
    resolver.setLocale(request, response, Locale.US)
    assertEquals(Locale.US, resolver.resolve(request))
  }
  
  @Test
  def setting_null_locale_resets_to_default() {
    resolver.setLocale(request, response, Locale.US)
    assertEquals(Locale.US, resolver.resolve(request))
    
    resolver.setLocale(request, response, null)
    assertEquals(defaultLocale, resolver.resolve(request))
  }
}
