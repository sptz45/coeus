/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import org.junit.Test
import org.junit.Assert._
import java.util._
import com.coeusweb.test.Assertions.assertThrows

class CollectionConverterTest {
  
  val locale: Locale = null
  val conv = CollectionConverter.forHashSet(new SimpleIntConverter)
  
  val set = new HashSet[Int]
  set.add(1)
  set.add(2) 
  
  @Test
  def null_maps_to_empty_collectiony() {
    assertEquals(0, conv.parse(null, locale).size)
    assertEquals("", conv.format(null, locale))
  }
  
  @Test
  def empty_string_maps_to_empty_collection() {
    assertEquals(0, conv.parse("", locale).size)
    assertEquals("", conv.format(new HashSet, locale))
  }
  
  @Test
  def parse_string_into_a_collection() {
    assertEquals(set, conv.parse("1,2", locale))
    assertEquals(set, conv.parse("1, 2", locale))
    assertEquals(set, conv.parse("1,   2 ", locale))
    set.remove(2)
    assertEquals(set, conv.parse("1,     ", locale))
  }
  
  @Test
  def format_collection_into_string() {
    assertEquals("1, 2", conv.format(set, locale))
    set.remove(2)
    assertEquals("1", conv.format(set, locale))
  }
  
  @Test
  def change_separator() {
    val conv = CollectionConverter.forHashSet(new SimpleIntConverter, separator=" ")
    assertEquals(set, conv.parse("1 2", locale))
    assertEquals(set, conv.parse("1  2  ", locale))
    assertThrows[NumberFormatException] { conv.parse("1,2", locale) }
  }
  
  @Test
  def do_not_append_space_when_formatting() {
    val conv = CollectionConverter.forHashSet(new SimpleIntConverter, appendSpace=false)
    assertEquals("1,2", conv.format(set, locale))
  }
}