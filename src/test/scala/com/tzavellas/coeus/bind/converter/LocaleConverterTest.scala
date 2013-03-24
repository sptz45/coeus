/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.bind.converter

import java.util.Locale
import org.junit.Test
import org.junit.Assert._

class LocaleConverterTest {

  val conv = new LocaleConverter
  
  @Test
  def test_default_locale() {
    assertEquals(Locale.US, conv.parse("  ", null))
    assertEquals(Locale.US, conv.parse(null, null))
  }
  
  @Test
  def test_locale_parsing() {
    assertEquals(Locale.US, conv.parse("en_US  ", null))
    assertEquals(Locale.US, conv.parse("en us", null))
    assertEquals(new Locale("el", "Gr"), conv.parse("el Gr", null))
  }
  
  @Test
  def test_locale_formatting() {
    assertEquals("en_US", conv.format(Locale.US, null))
  }
}
