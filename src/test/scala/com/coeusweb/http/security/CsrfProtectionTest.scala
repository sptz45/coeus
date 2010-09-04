/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.http.security

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.WebRequest
import com.coeusweb.i18n.locale.FixedLocaleResolver
import com.coeusweb.scope.RequiredAttributeException
import com.coeusweb.test.servlet.MockHttpServletRequest

class CsrfProtectionTest {
  
  val mock = new MockHttpServletRequest("POST", "/")
  val request = new WebRequest(null, mock, null, new FixedLocaleResolver(java.util.Locale.US), null, null)
  
  @Test
  def non_post_requests_are_not_checked() {
    mock.setMethod("GET")
    CsrfProtection.assertOrigin(request)
  }
  
  @Test
  def ajax_requests_are_not_checked() {
    mock.addHeader("X-Requested-With", "XMLHttpRequest")
    CsrfProtection.assertOrigin(request)
  }
  
  @Test(expected=classOf[IllegalStateException])
  def exception_when_session_does_not_exist() {
    CsrfProtection.assertOrigin(request)
  }
  
  @Test(expected=classOf[RequiredAttributeException])
  def exception_when_not_token_in_session() {
    createSession()
    CsrfProtection.assertOrigin(request)
  }
  
  @Test(expected=classOf[MaliciousRequestException])
  def exception_when_not_tokens_do_not_match() {
    val session = createSession()
    CsrfProtection.getToken(session)
    CsrfProtection.assertOrigin(request)
  }
  
  @Test
  def valid_request_when_tokens_match() {
    val session = createSession()
    val token = CsrfProtection.getToken(session)
    mock.addParameter(CsrfProtection.tokenName, token)
    CsrfProtection.assertOrigin(request)
  }
  
  @Test
  def same_token_is_returned_for_the_same_session() {
    request.session
    
    val token1 = CsrfProtection.getToken(request.session)
    val token2 = CsrfProtection.getToken(request.session)
    assertEquals(token1, token2)
    
    request.resetSession()
    
    val token3 = CsrfProtection.getToken(request.session)
    assertTrue(token1 != token3)
    
    val token4 = CsrfProtection.getToken(request.session)
    assertEquals(token3, token4)
  }
  
  def createSession() = request.session
}
