/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import java.util.Locale, java.math.BigDecimal
import java.text._


class BigDecimalConverter(pattern: Option[String] = None) extends AbstractConverter[BigDecimal] {
  
  def parse(text: String, locale: Locale) = filterEmpty(text, BigDecimal.ZERO, parseBigDecimal(_, locale)) 
  
  def format(decimal: BigDecimal, locale: Locale) =
    if (decimal eq null) "" else numberFormat(locale).format(decimal)
    
  private def parseBigDecimal(text: String, locale: Locale) = {
    val position = new ParsePosition(0)
    val number = numberFormat(locale).parse(text, position)
    if (position.getErrorIndex() != -1) {
      throw new ParseException(text, position.getIndex());
    }
    number.asInstanceOf[BigDecimal]
  }
  
  private def numberFormat(locale: Locale) = {
    val fmt = createNumberFormat(locale)
    fmt.setParseBigDecimal(true)
    for (p <- pattern) fmt.applyPattern(p)
    fmt
  }
  
  protected def createNumberFormat(locale: Locale) =
    NumberFormat.getInstance(locale).asInstanceOf[DecimalFormat]
}

