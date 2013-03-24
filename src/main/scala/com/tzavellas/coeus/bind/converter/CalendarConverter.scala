/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.bind.converter

import java.util.{Date, Calendar, TimeZone, Locale}
import java.text.{DateFormat, SimpleDateFormat}
import com.tzavellas.coeus.bind.Converter

class CalendarConverter(
  patternOrStyle: Either[String, Int] = Left("yy/MM/dd"),
  lenient: Boolean = false,
  timeZone: Option[TimeZone] = None)
    extends Converter[Calendar] {
  
  private[this] val converter = new DateConverter(patternOrStyle, lenient, timeZone)
  
  def parse(text: String, locale: Locale) = {
    val date = converter.parse(text, locale)
    if (date eq null) null else {
      val calendar = Calendar.getInstance
      calendar.setTime(date)
      calendar
    }
  }
  
  def format(calendar: Calendar, locale: Locale) = converter.format(calendar.getTime, locale)
}