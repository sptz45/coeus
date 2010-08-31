/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.util

/**
 * Helper methods for working with Strings.
 */
object Strings {

  def camelCaseToDashed(str: String): String = {
    val result = new StringBuilder(str.length)
    var i = 0
    while (i < str.length) {
      val c = str.charAt(i)
      if (c.isUpper) {
          if (i == 0) {
            result += c.toLower
          } else {
            result += '-'
            result += c.toLower
          }
      } else {
        result += c
      }
      i += 1
    }
    result.toString
  }
  
  def firstCharToLower(s: String): String = {
    if (s.isEmpty) s else {
      if (s(0).isLower) s else s(0).toLower + s.substring(1)
    }
  }
  
  def removeSuffix(str: String, suffix: String) = {
    if (str.endsWith(suffix))
      str.substring(0, str.length - suffix.length)
    else
      str
  }
  
  def removePrefix(str: String, prefix: String) = {
    if (str.startsWith(prefix))
      str.substring(prefix.length, str.length)
    else
      str
  } 
  
  def stripEndChars(s: String, c: Char): String = {
    var i = s.length - 1
    while(i >= 0) {
      if (s.charAt(i) != c)
        return s.substring(0, i + 1)
      i -= 1
    }
    return ""
  }
  
  def nullSafeToString(a: Any) = if (a == null) "null" else a.toString 
}
