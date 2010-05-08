/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import java.util.Locale
import com.coeusweb.bind.Converter

class SBigDecimalConverter(converter: BigDecimalConverter) extends Converter[BigDecimal] {
  
  def this() = this(new BigDecimalConverter)

  def parse(text: String, locale: Locale) = new BigDecimal(converter.parse(text, locale))
  
  def format(value: BigDecimal, locale: Locale) = converter.format(value.bigDecimal, locale)
}