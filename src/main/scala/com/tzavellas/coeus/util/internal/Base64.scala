/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.util.internal

import javax.xml.bind.DatatypeConverter

object Base64 {
  
  def encode(bytes: Array[Byte]): String = DatatypeConverter.printBase64Binary(bytes)
  
  def decode(msg: String): Array[Byte] = DatatypeConverter.parseBase64Binary(msg)
}