/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import java.util.{Date, Calendar, TimeZone, Locale}
import java.text.{DateFormat, SimpleDateFormat}
import com.coeusweb.bind.Converter

class CalendarConverter(
  patternOrStyle: Either[String, Int] = Left("yy/MM/dd"),
  lenient: Boolean = false,
  timeZone: Option[TimeZone] = None)
    extends Converter[Calendar] {
  
  private[this] val converter = new DateConverter(patternOrStyle, lenient, timeZone)
  
  def parse(string: String, locale: Locale) = {
    val calendar = Calendar.getInstance
    calendar.setTime(converter.parse(string, locale))
    calendar
  }
  
  def format(calendar: Calendar, locale: Locale) = converter.format(calendar.getTime, locale)
}