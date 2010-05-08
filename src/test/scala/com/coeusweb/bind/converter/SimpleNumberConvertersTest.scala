/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.test.Assertions.assertThrows

class SimpleNumberConvertersTest {
  
  val locale: java.util.Locale = null
  val delta = 0.0001
  
  @Test
  def test_ints() {
    val conv = new SimpleIntConverter
    
    assertEquals(0, conv.parse("\n \n", locale))
    assertEquals(0, conv.parse(null, locale))
    assertEquals(10, conv.parse("10", locale))
    assertEquals(10, conv.parse(" 10\n", locale))
    
    assertThrows[NumberFormatException] { conv.parse("0.2", locale) }
    assertThrows[NumberFormatException] { conv.parse("ten", locale) }
    
    assertEquals("10", conv.format(10, locale))
  }
  
  @Test
  def test_longs() {
    val conv = new SimpleLongConverter
    
    assertEquals(0L, conv.parse("\n \n", locale))
    assertEquals(0L, conv.parse(null, locale))
    assertEquals(10L, conv.parse("10", locale))
    assertEquals(10L, conv.parse(" 10\n", locale))
    
    assertThrows[NumberFormatException] { conv.parse("0.2", locale) }
    assertThrows[NumberFormatException] { conv.parse("ten", locale) }
    
    assertEquals("10", conv.format(10, locale))
  }
  
  @Test
  def test_floats() {
    val conv = new SimpleFloatConverter
    
    assertEquals(0, conv.parse("\n \n", locale), delta)
    assertEquals(0, conv.parse(null, locale), delta)
    assertEquals(10, conv.parse("10", locale), delta)
    assertEquals(10, conv.parse(" 10\n", locale), delta)
    assertEquals(0.2, conv.parse(" 0.2\n", locale), delta)
    
    assertThrows[NumberFormatException] { conv.parse("ten", locale) }
    
    assertEquals("10.0", conv.format(10, locale))
  }
  
  @Test
  def test_doubles() {
    val conv = new SimpleDoubleConverter
    
    assertEquals(0, conv.parse("\n \n", locale), delta)
    assertEquals(0, conv.parse(null, locale), delta)
    assertEquals(10, conv.parse("10", locale), delta)
    assertEquals(10, conv.parse(" 10\n", locale), delta)
    assertEquals(0.2, conv.parse(" 0.2\n", locale), delta)
    
    assertThrows[NumberFormatException] { conv.parse("ten", locale) }
    
    assertEquals("10.0", conv.format(10, locale))
  }
}
