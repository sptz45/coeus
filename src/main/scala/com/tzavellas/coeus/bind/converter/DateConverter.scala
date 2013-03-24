/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.bind.converter

import java.util.{Date, TimeZone, Locale}
import java.text.{DateFormat, SimpleDateFormat}
import com.tzavellas.coeus.bind.Converter

class DateConverter(
  patternOrStyle: Either[String, Int] = Left("yyyy/MM/dd"),
  lenient: Boolean = false,
  timeZone: Option[TimeZone] = None)
    extends AbstractConverter[Date] {
  
  def parse(text: String, locale: Locale) =
    filterEmpty(text, null, dateFormat(locale).parse(_))
  
  def format(value: Date, locale: Locale) = dateFormat(locale).format(value)
    
  private def dateFormat(locale: Locale) = {
    val fmt = patternOrStyle match {
      case Left(pattern) => new SimpleDateFormat(pattern, locale)
      case Right(style)  => DateFormat.getDateInstance(style, locale) 
    }
    for (tz <- timeZone) fmt.setTimeZone(tz)
    fmt.setLenient(lenient)
    fmt
  }
}

