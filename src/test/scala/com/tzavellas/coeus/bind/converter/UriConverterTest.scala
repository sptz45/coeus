/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.bind.converter

import java.net.URI
import org.junit.Test
import org.junit.Assert._

class UriConverterTest {

  val locale: java.util.Locale = null
  val conv = new UriConverter
  val google = new URI("http://google.com")
  
  @Test(expected=classOf[NullPointerException])
  def parsing_null() {
    conv.parse(null, locale)
  }
  
  @Test
  def parsing() {
    assertEquals(google, conv.parse("http://google.com", locale))
  }
  
  @Test
  def formatting() {
    assertEquals("", conv.format(null, locale))
    assertEquals(google.toString, conv.format(google, locale))
  }
}
