/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.http.security

import java.security.{ MessageDigest, SignatureException }
import javax.crypto.{ Cipher, Mac }
import javax.crypto.spec.{ SecretKeySpec, IvParameterSpec }
import scala.compat.Platform

abstract class AbstractCryptoProvider extends CryptoProvider {

  def signingAlgorithm: String
  
  def encryptionAlgorithm: String
  
  def encryptionMode: String
    
  def encryptionPadding: String
  
  def encryptionKeyLength: Int
  
  val initVector: IvParameterSpec
  
  def encrypt(msg: String, key: Array[Byte]): Array[Byte] = {
    val skeySpec = new SecretKeySpec(processKey(key), encryptionAlgorithm)
    val cipher = Cipher.getInstance(cipherAlgorithm)
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, initVector)
    cipher.doFinal(msg.getBytes)
  }
  
  def decrypt(msg: Array[Byte], key: Array[Byte]): String = {
    val skeySpec = new SecretKeySpec(processKey(key), encryptionAlgorithm)
    val cipher = Cipher.getInstance(cipherAlgorithm)
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, initVector)
    new String(cipher.doFinal(msg))
  }

  def sign(data: Seq[String], key: Array[Byte]): Array[Byte] = {
    val signingKey = new SecretKeySpec(key, signingAlgorithm)
    val mac = Mac.getInstance(signingAlgorithm)
    mac.init(signingKey)

    for (elem <- data) mac.update(elem.getBytes)      
    mac.doFinal()
  }
  
  def verify(signature: Array[Byte], data: Seq[String], key: Array[Byte]) = {
    MessageDigest.isEqual(signature, sign(data, key))
  }
  
  private def processKey(key: Array[Byte]) = {
    
    val KeyLength = encryptionKeyLength / 8 
    
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
      case KeyLength                    => key
      case length if length > KeyLength => truncate
      case length if length < KeyLength => pad
    }
  }
  
  private val cipherAlgorithm = encryptionAlgorithm + "/" + encryptionMode + "/" + encryptionPadding
}