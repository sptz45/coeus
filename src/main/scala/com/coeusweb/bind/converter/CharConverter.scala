/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import java.util.Locale

class CharConverter extends AbstractConverter[Char] {
  
  def parse(text: String, locale: Locale) = filterEmpty(text, 0, _.apply(0))
  
  def format(char: Char, locale: Locale) = char.toString
} 

