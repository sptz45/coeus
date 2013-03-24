/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.bind.converter

import java.util.Locale
import com.tzavellas.coeus.bind.Converter

/**
 * A Converter for String objects that trims the input when formating and parsing
 */
class StringTrimConverter extends Converter[String] {
  
   /**
   * Trim the input text
   */
  def parse(text: String, locale: Locale) = if (text eq null) "" else text.trim
  
  /**
   * Trim the input value
   */
  def format(value: String, locale: Locale) = parse(value, locale)
}