/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.bind.converter

import java.util.{Currency, Locale}
import java.math.{BigDecimal, RoundingMode}
import java.text._


class CurrencyConverter(
  fractionDigits: Int = 2,
  roundingMode: RoundingMode = RoundingMode.UNNECESSARY,
  currency: Option[Currency] = None)
    extends BigDecimalConverter(None) {
  
  override def parse(text: String, locale: Locale) =
    filterEmpty(text, BigDecimal.ZERO, parseCurrency(_, locale))
  
  def parseCurrency(text: String, locale: Locale) = {
    var decimal = super.parse(text, locale)
    decimal = decimal.setScale(this.fractionDigits, this.roundingMode);
    decimal   
  }

  
  override protected def createNumberFormat(locale: Locale) = {
    val fmt = NumberFormat.getCurrencyInstance(locale).asInstanceOf[DecimalFormat]
    fmt.setParseBigDecimal(true)
    fmt.setMaximumFractionDigits(fractionDigits)
    fmt.setMinimumFractionDigits(fractionDigits)
    fmt.setRoundingMode(this.roundingMode)
    for (c <- currency) fmt.setCurrency(c)
    fmt
  }
}
