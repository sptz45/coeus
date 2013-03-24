/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Inspired and based on Rails' DateHelper.distance_of_time_in_words
 * helper method.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.view.helper

import java.util.{Date, Calendar}


private object TimeDistanceFormat {
 
  def format(date: Date): String = format(date.getTime)
  
  def format(calendar: Calendar): String = format(calendar.getTime)
  
  def format(time: Long): String = {
    
    val now = (new Date).getTime
    val isInThePast = if (time <= now) true else false
    
    val distanceInSeconds = (time / 1000 - now / 1000).abs
    val distanceInMinutes = (distanceInSeconds / 60d).round
    
    if (distanceInMinutes <= 1) {
      return distanceInSeconds match {
        case Less_Than_5_Secs(_)   => lessThanXSecondsMsg(5, isInThePast)
        case Less_Than_10_Secs(_)  => lessThanXSecondsMsg(10, isInThePast)
        case Less_Than_20_Secs(_)  => lessThanXSecondsMsg(20, isInThePast)
        case Half_A_Minute(_)      => msg("half a minute", null, null, 0, isInThePast)
        case Less_Than_1_Minute(_) => msg("less than ", " minute", " minutes", 1, isInThePast)
        case _                     => msg("", " minute", " minutes", 1, isInThePast)
      }
    }
    
    distanceInMinutes match {
      case Minutes(x)        => msg("", "minute", "minutes", x, isInThePast)
      case About_X_Hours(x)  => msg("about ", " hour", " hours", x, isInThePast)
      case About_X_Days(x)   => msg("about ", " day", " days", x, isInThePast)
      case About_X_Months(x) => msg("about ", " month", " months", x, isInThePast)
      case One_Year(_)       => msg("about ", " year", " years", 1, isInThePast)
      case Over_X_Years(x)   => msg("over ", " year", " years", x, isInThePast)
    }
  }
  
  object Less_Than_5_Secs extends SimpleTimeTest(-1, 4)
  object Less_Than_10_Secs extends SimpleTimeTest(4, 9)
  object Less_Than_20_Secs extends SimpleTimeTest(9, 19)
  object Half_A_Minute extends SimpleTimeTest(19, 39)
  object Less_Than_1_Minute extends SimpleTimeTest(39, 59)
  
  object Minutes extends SimpleTimeTest(2, 44)
  
  object About_X_Hours extends TimeTest(44, 1439) {
    def convert(minutes: Long) = (minutes / 60d).round
  }
  object About_X_Days extends TimeTest(1439, 43199) {
    def convert(minutes: Long) = (minutes / 1440d).round 
  }
  object About_X_Months extends TimeTest(43199, 525599) {
    def convert(minutes: Long) = (minutes / 43829d).round
  }
  object One_Year extends TimeTest(525599, 1051199) {
    def convert(minutes: Long) = 1 
  }
  object Over_X_Years extends TimeTest(1051199, Long.MaxValue) {
    def convert(minutes: Long) = (minutes / 525600d).round 
  }
  
  class SimpleTimeTest(min: Long, max: Long) extends TimeTest(min, max) {
    def convert(time: Long) = time
  }
  
  abstract class TimeTest(min: Long, max: Long) {
    def unapply(minutes: Long) =
      if (minutes > min && minutes <= max) Some(convert(minutes)) else None
      
      def convert(time: Long): Long
  }
  
  //TODO support for i18n
  private def msg(prefix: String, singular: String, plural: String, distance: Long, isInThePast: Boolean) = {
    val sb = new java.lang.StringBuilder(prefix)
    if (distance != 0) sb.append(distance)
    if (distance > 1) sb.append(plural)
    else if (distance == 1) sb.append(singular)
    sb.append(if (isInThePast) " ago" else " ahead")
    sb.toString
  }
  
  private def lessThanXSecondsMsg(x: Long, isInThePast: Boolean) = {
    msg("less than ", " second", " seconds", x, isInThePast)
  }
}