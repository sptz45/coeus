/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.bind.converter

import java.util.{Currency, Locale}
import com.tzavellas.coeus.bind.Converter

class CurrencySymbolConverter extends Converter[Currency] {

  def parse(text: String, locale: Locale) = {
    require(text ne null)
    Currency.getInstance(text.trim.toUpperCase)
  }
  
  def format(currency: Currency, locale: Locale) = currency.getSymbol(locale)
}
