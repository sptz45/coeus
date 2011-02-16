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
  
  def time_distance(calendar: Calendar) = TimeDistanceFormat.format(calendar)
  
  def time_tag_human(date: Date) = <time datetime={timeTagFormat.format(date)}>{time_distance(date)}</time>
  
  def timeTagFormat = {
    val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    format.setTimeZone(TimeZone.getTimeZone("UTC"))
    format
  }
}