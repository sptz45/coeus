/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import org.junit.Test
import org.junit.Assert._

class StringTrimConverterTest {

  val converter = new StringTrimConverter
  val parse = converter.parse(_: String, null)
  val format = converter.format(_: String, null)
  
  @Test
  def simple_test() {
    assertEquals3("", parse(""), format(""))
    assertEquals3("", parse(null), format(null))
    assertEquals3("trim", parse(" trim "), format(" trim "))
    assertEquals3("right trim", parse("right trim "), format("right trim "))
    assertEquals3("left trim", parse(" left trim"), format(" left trim"))
  }
  
  def assertEquals3(expected: String, parsed: String, formatted: String) {
    assertEquals(expected, parsed)
    assertEquals(expected, formatted)
  }
}
