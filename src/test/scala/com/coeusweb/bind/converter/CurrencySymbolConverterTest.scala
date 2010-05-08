/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import java.util.{Currency, Locale}
import org.junit.Test
import org.junit.Assert._

class CurrencyConverterrTest {

  val locale = Locale.GERMANY
  val euro = Currency.getInstance("EUR")
  val conv = new CurrencySymbolConverter
  
  @Test
  def simple_test() {
    assertEquals(euro, conv.parse("eur", locale))
    assertEquals(euro, conv.parse("EUR", locale))
    assertEquals(euro, conv.parse(" EUR ", locale))
  }
  
  @Test(expected=classOf[IllegalArgumentException])
  def null_handling() {
    conv.parse(null, locale)
  }
}
