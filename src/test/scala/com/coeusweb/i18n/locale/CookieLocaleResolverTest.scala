/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.i18n.locale

import java.util.Locale
import javax.servlet.http.Cookie
import org.junit.{Test, Before}
import org.junit.Assert._
import org.springframework.mock.web._

class CookieLocaleResolverTest {

  val requestLocale = Locale.UK
  val request = new MockHttpServletRequest
  val response = new MockHttpServletResponse
  val resolver = new CookieLocaleResolver
  
  @Before
  def setLocaleInHttpRequest() {
    request.addPreferredLocale(requestLocale)
  }
  
  @Test
  def use_default_locale_if_no_cookie_in_request() {
    assertEquals(Locale.US, new CookieLocaleResolver(Some(Locale.US)).resolve(request))
  }
  
  @Test
  def use_request_locale_if_if_no_cookie_in_request_and_default_not_specified() {
    assertEquals(Locale.UK, resolver.resolve(request))
  }
  
  @Test
  def read_the_locale_from_cookie() {
    request.setCookies(new Cookie(resolver.COOKIE_NAME, Locale.US.toString))
    assertEquals(Locale.US, resolver.resolve(request))
  }
  
  @Test
  def set_the_locale_to_a_cookie() {
    resolver.setLocale(request, response, Locale.US)
    assertEquals(Locale.US, resolver.resolve(request))
    assertNotNull(response.getCookie(resolver.COOKIE_NAME))
  }
  
  @Test
  def setting_the_locale_to_null_resets_to_default_locale() {
    resolver.setLocale(request, response, Locale.US)
    assertEquals(Locale.US, resolver.resolve(request))
    
    resolver.setLocale(request, response, null)
    assertEquals(requestLocale, resolver.resolve(request))
  }
}
