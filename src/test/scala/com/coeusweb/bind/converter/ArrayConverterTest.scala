package com.coeusweb.bind.converter

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.test.Assertions.assertThrows

class ArrayConverterTest {
  
  val locale: java.util.Locale = null
  val conv = new ArrayConverter(new SimpleIntConverter)
  
  @Test
  def null_maps_to_empty_array() {
    assertEquals(0, conv.parse(null, locale).size)
    assertEquals("", conv.format(null, locale))
  }
  
  @Test
  def empty_string_maps_to_empty_array() {
    assertEquals(0, conv.parse("", locale).size)
    assertEquals("", conv.format(Array[Int](), locale))
  }
  
  @Test
  def parse_string_into_int_array() {
    assertEquals(2, conv.parse("1,2", locale).size)
    assertEquals(2, conv.parse("1, 2", locale).size)
    assertEquals(2, conv.parse("1,   2 ", locale).size)
    assertEquals(1, conv.parse("1,     ", locale).size)
  }
  
  @Test
  def format_array_into_string() {
    assertEquals("1, 2", conv.format(Array(1, 2), locale))
    assertEquals("1", conv.format(Array(1), locale))
  }
  
  @Test
  def change_separator() {
    val conv = new ArrayConverter(new SimpleIntConverter, separator=" ")
    assertEquals(2, conv.parse("1 2", locale).size)
    assertEquals(2, conv.parse("1  2  ", locale).size)
    assertThrows[NumberFormatException] { conv.parse("1,2", locale) }
  }
  
  @Test
  def do_not_append_space_when_formatting() {
    val conv = new ArrayConverter(new SimpleIntConverter, appendSpace=false)
    assertEquals("1,2", conv.format(Array(1,2), locale))
  }
}
