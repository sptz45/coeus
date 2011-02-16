/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.view.helper

import java.util.{ Date, Calendar, TimeZone }
import java.text.SimpleDateFormat

trait DateHelper {

  def time_distance(date: Date) = TimeDistanceFormat.format(date)
  
  def time_distance(cal: Calendar) = TimeDistanceFormat.format(cal)
  
  def time_tag_distance(date: Date) = {
    <time datetime={timeTagFormat.format(date)}>{time_distance(date)}</time>
  }
  
  def time_tag_distance(cal: Calendar) = {
    <time datetime={format_datetime(cal)}>{time_distance(cal)}</time>
  }
  
  def format_datetime(date: Date) = timeTagFormat.format(date)
  
  def format_datetime(cal: Calendar) = timeTagFormat.format(cal.getTime)
  
  private def timeTagFormat = {
    val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    format.setTimeZone(TimeZone.getTimeZone("UTC"))
    format
  }
}