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
class StringConverter extends Converter[String] {
  
   /**
   * Returns value with no modification
   */
  def parse(text: String, locale: Locale) = text
  
  /**
   * Returns value with no modification
   */
  def format(value: String, locale: Locale) = value
}