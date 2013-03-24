/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.util.internal

import org.junit.Test
import org.junit.Assert._

class StringsTest {
  
  import Strings._

  @Test
  def camel_case_to_dashed() {
    assertEquals("", camelCaseToDashed(""))
    assertEquals("hello", camelCaseToDashed("Hello"))
    assertEquals("hello-world", camelCaseToDashed("HelloWorld"))
    assertEquals("hello-scala-world", camelCaseToDashed("HelloScalaWorld"))
    assertEquals("hello-world", camelCaseToDashed("helloWorld"))
  }
  
  @Test
  def first_char_to_lower() {
    assertEquals("", firstCharToLower(""))
    assertEquals("hello", firstCharToLower("Hello"))
    assertEquals("helloWorld", firstCharToLower("HelloWorld"))
  }
  
  @Test
  def remove_suffix() {
    assertEquals("", removeSuffix("", ""))
    assertEquals("", removeSuffix("", "world"))
    assertEquals("hello", removeSuffix("hello", "world"))
    assertEquals("hello", removeSuffix("helloworld", "world"))
    assertEquals("helloworld", removeSuffix("helloworld", "World"))
  }
  
  @Test
  def remove_prefix() {
    assertEquals("", removePrefix("", ""))
    assertEquals("", removePrefix("", "world"))
    assertEquals("hello", removePrefix("hello", "world"))
    assertEquals("world", removePrefix("helloworld", "hello"))
    assertEquals("helloworld", removePrefix("helloworld", "World"))
  }
  
  @Test
  def strip_end_chars() {
    assertEquals("", stripEndChars("", '/'))
    assertEquals("", stripEndChars("///////", '/'))
    assertEquals("/hello/world", stripEndChars("/hello/world//", '/'))
    assertEquals("/hello", stripEndChars("/hello", '/'))
  }
  
  @Test
  def null_safe_tos_string() {
    assertEquals("null", nullSafeToString(null))
    assertEquals("null", nullSafeToString("null"))
    assertEquals("2", nullSafeToString(2))
  }
}