/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import java.util.Locale
import java.text._

class PercentConverter(pattern: Option[String] = None) extends BigDecimalConverter(pattern) {
  
  override protected def createNumberFormat(locale: Locale) =
    NumberFormat.getPercentInstance(locale).asInstanceOf[DecimalFormat]
}