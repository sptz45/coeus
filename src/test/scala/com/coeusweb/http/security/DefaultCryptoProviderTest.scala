/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.http.security

import org.junit.Test
import org.junit.Assert._

class DefaultCryptoProviderTest {
  
  def crypto = new DefaultCryptoProvider
  
  @Test
  def generate_a_signature_and_verify_it() {
    val message = List("I", "love", "Scala")
    val key = "the-secret".getBytes
    val signature = crypto.sign(message, key)
    assertTrue(crypto.verify(signature, message, key))
  }
  
  @Test
  def encrypt_and_decrypt_data_with_smaller_key() {
    val message = "I love Scala"
    val key = "the-secret".getBytes
    val encrypted = crypto.encrypt(message, key)
    assertEquals(message, crypto.decrypt(encrypted, key))
  }
  
  @Test
  def encrypt_and_decrypt_data_with_larger_key() {
    val message = "I love Scala"
    val key = crypto.sign(List("some data"), "big secret".getBytes)
    val encrypted = crypto.encrypt(message, key)
    assertEquals(message, crypto.decrypt(encrypted, key))
  }
}