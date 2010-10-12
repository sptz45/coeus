/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.http.security

import java.security.{ MessageDigest, SignatureException }
import javax.crypto.{ Cipher, Mac }
import javax.crypto.spec.SecretKeySpec
import scala.compat.Platform

/**
 * The default implementation of <code>CryptoProvider</code>.
 * 
 * <p>For signing this implementation uses <em>HMAC-SHA1</em> and for
 * encryption <em>AES</em> with 128bit keys.</p>
 */
object DefaultCryptoProvider extends CryptoProvider {
  
  private val SIGNING_ALGORITHM = "HmacSHA1"
  private val ENCRYPTION_ALGORITHM = "AES"
  
  def encrypt(msg: String, key: Array[Byte]): Array[Byte] = {
    val skeySpec = new SecretKeySpec(processKey(key), ENCRYPTION_ALGORITHM)
    val cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM)
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
    cipher.doFinal(msg.getBytes)
  }
  
  def decrypt(msg: Array[Byte], key: Array[Byte]): String = {
    val skeySpec = new SecretKeySpec(processKey(key), ENCRYPTION_ALGORITHM)
    val cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM)
    cipher.init(Cipher.DECRYPT_MODE, skeySpec)
    new String(cipher.doFinal(msg))
  }

  def sign(data: Seq[String], key: Array[Byte]): Array[Byte] = {
    val signingKey = new SecretKeySpec(key, SIGNING_ALGORITHM)
    val mac = Mac.getInstance(SIGNING_ALGORITHM)
    mac.init(signingKey)

    for (elem <- data) mac.update(elem.getBytes)      
    mac.doFinal()
  }
  
  def verify(signature: Array[Byte], data: Seq[String], key: Array[Byte]) = {
    MessageDigest.isEqual(signature, sign(data, key))
  }
  
  /* Make sure the key has 128 bits (16 bytes) length. */
  private def processKey(key: Array[Byte]) = {
    def truncate = {
      val newKey = new Array[Byte](16)
      Platform.arraycopy(key, 0, newKey, 0, newKey.length)
      newKey
    }
    def pad = {
      val newKey = new Array[Byte](16)
      Platform.arraycopy(key, 0, newKey, 0, key.length)
      for (i <- key.length until 16) {
        newKey(i) = 0
      }
      newKey
    }
    key.length match {
      case 16 => key
      case length if length > 16 => truncate
      case length if length < 16 => pad
    }
  }
}