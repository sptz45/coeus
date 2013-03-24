/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.http.security

import javax.servlet.http.Cookie
import org.junit.Test
import org.junit.Assert._

class CookieSignerTest {

  val cookies = new CookieSigner("the secret", new DefaultCryptoProvider)
  val cookie = new Cookie("price", "100")
  cookie.setMaxAge(1000)
  
  @Test
  def make_secure_without_encryption() {
    cookies.signCookie(cookie)
    assertEquals(Some("100"), cookies.getSignedValue(cookie))
  }
  
  @Test
  def make_secure_with_userId_and_without_encryption() {
    cookies.signCookie(cookie, userId="user")
    assertEquals(Some("100"), cookies.getSignedValue(cookie))
  }
  
  @Test
  def make_secure_with_encryption() {
    cookies.signCookie(cookie, encrypt=true, userId="user")
    assertEquals(Some("100"), cookies.getSignedValue(cookie, isEncrypted=true))
  }
  
  @Test
  def make_secure_without_timeout() {
    cookie.setMaxAge(-1)
    cookies.signCookie(cookie)
    assertEquals(Some("100"), cookies.getSignedValue(cookie))
  }
  
  @Test
  def cookie_has_expired() {
    cookie.setMaxAge(0)
    cookies.signCookie(cookie)
    Thread.sleep(1)
    assertEquals(None, cookies.getSignedValue(cookie))
  }
  
  @Test(expected=classOf[MaliciousRequestException])
  def cookie_has_wrong_signature() {
    cookies.signCookie(cookie)
    
    val values = cookie.getValue.split("\\|")
    values(2) = "wrong signature"
    cookie.setValue(values.mkString("|"))
    
    cookies.getSignedValue(cookie)
  }
  
  @Test(expected=classOf[MaliciousRequestException])
  def cookie_has_no_separators() {
    cookie.setValue("malformed cookie")
    cookies.getSignedValue(cookie)
  }
  
  @Test(expected=classOf[MaliciousRequestException])
  def cookie_has_no_invalid_timeout() {
    cookie.setValue("invalid-timeout|value|signature")
    cookies.getSignedValue(cookie)
  }
}