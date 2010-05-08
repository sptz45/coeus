/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import java.nio.charset.Charset
import java.util.Locale
import com.coeusweb.bind.Converter

class CharsetConverter extends Converter[Charset] {

  def parse(text: String, locale: Locale) = Charset.forName(text)
  
  def format(charset: Charset, locale: Locale) = charset.toString
}