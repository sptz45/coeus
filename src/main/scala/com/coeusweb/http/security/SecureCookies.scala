/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.http.security

import javax.servlet.http.Cookie
import scala.collection.mutable.ArrayBuffer
import com.coeusweb.util.internal.Base64

/**
 * Helper methods for signing and encrypting cookies.
 * 
 * <p>This is a partial implementation of the Secure Cookie Protocol as
 * described in "A Secure Cookie Protocol" by Alex X. Liu1, Jason M. Kovacs,
 * Chin-Tser Huang and Mohamed G. Gouda. We've implemented the protocol except
 * the protection from replay attacks because that required using the session-id
 * in the generation of the cookie signature thus coupling the lifetime of the
 * cookie to the lifetime of the session.</p>
 * 
 * @param secretKey a secret key to be used in the generation of the signing and
 *        encryption key.
 * @param crypto the framework's configured strategy for encrypting and signing
 *        data.
 */
class SecureCookies(secretKey: String, crypto: CryptoProvider) {
  
  private[this] val secret = secretKey.getBytes
  
  /**
   * Signs and optionally encrypts the value of the cookie.
   * 
   * <p>The format of the cookie's value after calling the method is:<br/>
   * <code>user-id|expiration-time|data|signature</code></p>
   * 
   * <p>If the {@code userId} is not specified then it in not used in the generated
   * value of the cookie. The <em>expiration-time</em> is calculated as milliseconds from
   * UNIX epoc using the current time and the {@code maxAge} value of the specified cookie.
   * If the {@code maxAge} is negative then it is not used in the generated cookie value.
   * <em>Data<em> contains the original value of the specified cookie. If {@code encrypt} is
   * {@code true} then the value contained in <em>data</em> gets gets encrypted using a
   * generated key.</p>  
   * 
   * @param cookie the cookie to sign and optionally encrypt
   * @param encrypt whether to encrypt the original value of the cookie
   * @param userId an identification of the user the specified cookie belongs to. It can be
   *        used in the generated signing key to enhance the security of the protocol.
   */
  def makeSecure(cookie: Cookie, encrypt: Boolean = false, userId: String = null) {
    
    val data = new ArrayBuffer[String](4)
    
    if (userId != null) {
      data += userId
    }
    
    if (cookie.getMaxAge >= 0) {
      data += (System.currentTimeMillis + (cookie.getMaxAge * 1000)).toString
    }
    
    val key = generateKey(data)

    if (encrypt) {
      data += Base64.encode(crypto.encrypt(cookie.getValue, key))
    } else {
      data += cookie.getValue
    }
    
    val signature = crypto.sign(data, key)
    data += Base64.encode(signature)
    
    cookie.setValue(data.mkString("|"))
  }
  
  /**
   * Read a singed value from the specified cookie.
   * 
   * @param cookie the cookie to read the value from
   * @param isEncrypted whether to decrypt the value of the cookie before returning it
   * @param userId an identification of the user the given cookie belongs to (optional).  
   * 
   * @return the value of the cookie of None if the cookie has expired.
   * @throws MaliciousRequestException if the cookie does not have the expected format or the
   *         signature of the cookie is invalid.  
   */
  def getSecuredValue(cookie: Cookie, isEncrypted: Boolean = false, userId: String = null): Option[String] = {
    
    val input = cookie.getValue.split("\\|")
    var index = 0 
    val data = new ArrayBuffer[String](3)
    
    try {
      if (userId ne null) {
        if (userId != input(index))
          throw new MaliciousRequestException("Cookie contains unexpected user: " + input(index))
        data += userId
        index += 1
      }

      if (cookie.getMaxAge >= 0) {
        val expirationTime = input(index).toLong
        val now = System.currentTimeMillis 
        if (now > expirationTime)
          return None
        data += expirationTime.toString
        index += 1
      }

      val key = generateKey(data)

      data += input(index)
      index += 1

      if (!crypto.verify(Base64.decode(input(index)), data, key))
        throw new MaliciousRequestException("Signature verification failed on cookie")

      val value =
        if (isEncrypted) crypto.decrypt(Base64.decode(data.last), key)
        else data.last

      Some(value)
      
    } catch {
      case e: MaliciousRequestException => throw e
      case e: Exception => throw new MaliciousRequestException("Malformed cookie")
    }
  }

  private def generateKey(keyData: Seq[String])= crypto.sign(keyData, secret)
}