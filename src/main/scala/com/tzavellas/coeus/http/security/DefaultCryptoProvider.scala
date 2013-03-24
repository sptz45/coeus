/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.http.security

import javax.crypto.spec.IvParameterSpec

/**
 * The default implementation of <code>CryptoProvider</code>.
 * 
 * <p>For signing this implementation uses <em>HMAC-SHA1</em> and for
 * encryption <em>AES</em> with 128bit keys.</p>
 */
class DefaultCryptoProvider extends AbstractCryptoProvider {

  def signingAlgorithm = "HmacSHA1"

  def encryptionAlgorithm = "AES"

  def encryptionMode = "CBC"

  def encryptionPadding = "PKCS5Padding"

  def encryptionKeyLength = 128

  val initVector = new IvParameterSpec(Array(
    7, 124, -33, -2, 90, 39, 88, 40, 100, 3, 65, -78, -13, 87, 56, 41))
}