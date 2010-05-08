/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.http.security

import javax.servlet.http.Cookie
import org.junit.Test
import org.junit.Assert._

class SecureCookiesTest {

  val security = new SecureCookies("the secret", DefaultCryptoProvider)
  val cookie = new Cookie("price", "100")
  cookie.setMaxAge(1000)
  
  @Test
  def make_secure_without_encryption() {
    security.makeSecure(cookie)
    assertEquals(Some("100"), security.getSecuredValue(cookie))
  }
  
  @Test
  def make_secure_with_userId_and_without_encryption() {
    security.makeSecure(cookie, userId="user")
    assertEquals(Some("100"), security.getSecuredValue(cookie, userId="user"))
  }
  
  @Test
  def make_secure_with_encryption() {
    security.makeSecure(cookie, encrypt=true, userId="user")
    assertEquals(Some("100"), security.getSecuredValue(cookie, isEncrypted=true, userId="user"))
  }
  
  @Test
  def make_secure_without_timeout() {
    cookie.setMaxAge(-1)
    security.makeSecure(cookie)
    assertEquals(Some("100"), security.getSecuredValue(cookie))
  }
  
  @Test
  def cookie_has_expired() {
    cookie.setMaxAge(0)
    security.makeSecure(cookie)
    Thread.sleep(1)
    assertEquals(None, security.getSecuredValue(cookie))
  }
  
  @Test(expected=classOf[MaliciousRequestException])
  def cookie_has_wrong_userid() {
    security.makeSecure(cookie, userId="bad guy")
    assertEquals(None, security.getSecuredValue(cookie, userId="good guy"))
  }
  
  @Test(expected=classOf[MaliciousRequestException])
  def cookie_has_wrong_signature() {
    security.makeSecure(cookie)
    
    val values = cookie.getValue.split("\\|")
    values(2) = "wrong signature"
    cookie.setValue(values.mkString("|"))
    
    security.getSecuredValue(cookie)
  }
  
  @Test(expected=classOf[MaliciousRequestException])
  def cookie_has_no_separators() {
    cookie.setValue("malformed cookie")
    security.getSecuredValue(cookie)
  }
  
  @Test(expected=classOf[MaliciousRequestException])
  def cookie_has_no_invalid_timeout() {
    cookie.setValue("invalid-timeout|value|signature")
    security.getSecuredValue(cookie)
  }
}