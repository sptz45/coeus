/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.bind.converter

import java.util.Locale

class BooleanConverter extends AbstractConverter[Boolean] {
  
  def parse(text: String, locale: Locale) = filterEmpty(text, false, _.toBoolean)
  
  def format(boolean: Boolean, locale: Locale) = boolean.toString
  
  private def doParse(text: String) = text.toLowerCase match {
    case "true" | "yes" | "1" => true
    case "false" | "no" | "0" => false
    case _ => throw new NumberFormatException("Value %s cannot get converted to boolean." format text)
  }
}
