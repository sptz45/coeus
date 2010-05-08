/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import java.util.Locale

class LocaleConverter(default: Locale = Locale.US) extends AbstractConverter[Locale] {
  
  private val LocaleRegex = """(\p{Alpha}*)[^\p{Alpha}]*(\p{Alpha}*)""".r
  
  def parse(text: String, locale: Locale) = filterEmpty(text, default, str2locale)
  
  def format(value: Locale, locale: Locale) = value.toString
  
  private def str2locale(text: String) = {
    text match {
      case LocaleRegex(language, country) => new Locale(language, country)
      case _ => new Locale(text)
    }
  }
}
