package com.coeusweb.bind.converter

import org.junit.Test
import org.junit.Assert._
import scala.collection.immutable.HashSet
import com.coeusweb.test.Assertions.assertThrows

class CollectionConverterTest {
  
  val locale: java.util.Locale = null
  val conv = CollectionConverter.fromCompanion(HashSet, new SimpleIntConverter)
  
  val set = HashSet(1,2)
  
  @Test
  def null_maps_to_empty_collectiony() {
    assertEquals(0, conv.parse(null, locale).size)
    assertEquals("", conv.format(null, locale))
  }
  
  @Test
  def empty_string_maps_to_empty_collection() {
    assertEquals(0, conv.parse("", locale).size)
    assertEquals("", conv.format(HashSet(), locale))
  }
  
  @Test
  def parse_string_into_a_collection() {
    assertEquals(set, conv.parse("1,2", locale))
    assertEquals(set, conv.parse("1, 2", locale))
    assertEquals(set, conv.parse("1,   2 ", locale))
    assertEquals(HashSet(1), conv.parse("1,     ", locale))
  }
  
  @Test
  def format_collection_into_string() {
    assertEquals("1, 2", conv.format(set, locale))
    assertEquals("1", conv.format(HashSet(1), locale))
  }
  
  @Test
  def change_separator() {
    val conv = CollectionConverter.fromCompanion(HashSet, new SimpleIntConverter, separator=" ")
    assertEquals(set, conv.parse("1 2", locale))
    assertEquals(set, conv.parse("1  2  ", locale))
    assertThrows[NumberFormatException] { conv.parse("1,2", locale) }
  }
  
  @Test
  def do_not_append_space_when_formatting() {
    val conv = CollectionConverter.fromCompanion(HashSet, new SimpleIntConverter, appendSpace=false)
    assertEquals("1,2", conv.format(set, locale))
  }
}
