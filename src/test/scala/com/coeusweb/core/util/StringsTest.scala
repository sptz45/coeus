/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.util

import org.junit.Test
import org.junit.Assert._

class StringsTest {

  @Test
  def camel_case_to_dashed() {
    assertEquals("", Strings.camelCaseToDashed(""))
    assertEquals("hello", Strings.camelCaseToDashed("Hello"))
    assertEquals("hello-world", Strings.camelCaseToDashed("HelloWorld"))
    assertEquals("hello-scala-world", Strings.camelCaseToDashed("HelloScalaWorld"))
    assertEquals("hello-world", Strings.camelCaseToDashed("helloWorld"))
  }
  
  @Test
  def first_char_to_lower() {
    assertEquals("", Strings.firstCharToLower(""))
    assertEquals("hello", Strings.firstCharToLower("Hello"))
    assertEquals("helloWorld", Strings.firstCharToLower("HelloWorld"))
  }
  
  @Test
  def remove_suffix() {
    assertEquals("", Strings.removeSuffix("", ""))
    assertEquals("", Strings.removeSuffix("", "world"))
    assertEquals("hello", Strings.removeSuffix("hello", "world"))
    assertEquals("hello", Strings.removeSuffix("helloworld", "world"))
    assertEquals("helloworld", Strings.removeSuffix("helloworld", "World"))
  }
  
  @Test
  def remove_prefix() {
    assertEquals("", Strings.removePrefix("", ""))
    assertEquals("", Strings.removePrefix("", "world"))
    assertEquals("hello", Strings.removePrefix("hello", "world"))
    assertEquals("world", Strings.removePrefix("helloworld", "hello"))
    assertEquals("helloworld", Strings.removePrefix("helloworld", "World"))
  }
  
  @Test
  def strip_end_chars() {
    assertEquals("", Strings.stripEndChars("", '/'))
    assertEquals("", Strings.stripEndChars("///////", '/'))
    assertEquals("/hello/world", Strings.stripEndChars("/hello/world//", '/'))
    assertEquals("/hello", Strings.stripEndChars("/hello", '/'))
  }
  
  @Test
  def null_safe_tos_string() {
    assertEquals("null", Strings.nullSafeToString(null))
    assertEquals("null", Strings.nullSafeToString("null"))
    assertEquals("2", Strings.nullSafeToString(2))
  }
}