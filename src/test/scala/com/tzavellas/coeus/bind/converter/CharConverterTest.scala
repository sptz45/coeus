/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.bind.converter

import org.junit.Test
import org.junit.Assert._

class CharConverterTest {
  
  val locale: java.util.Locale = null
  val conv = new CharConverter
  
  @Test
  def simple_test() {
    assertEquals('a', conv.parse("\n a\n", locale))
    assertEquals(0, conv.parse(null, locale))
    assertEquals('1', conv.parse("10", locale))
    assertEquals('a', conv.parse(" a\n", locale))
    
    assertEquals("a", conv.format('a', locale))
  }
}
