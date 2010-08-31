/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.util

import com.coeusweb.core.FrameworkException

/**
 * Functions that perform variable interpolation in Strings.
 */
object Interpolator {
  

  /** The default formatter function. */
  val defaultFormatter: Any => String = {
    case null     => "null"
    case any: Any => any.toString
  }
  
  /**
   * Substitute any variables in the <code>msg</code> string by using
   * the values found in the <code>args</code> sequence formatted with the
   * <code>format</code> function.
   * 
   * <p>For example:</p><code>interpolate("Hello {world}", List("World"))</code>
   * will evaluate to the string: "Hello World".</p>
   */
  def interpolateVars(msg: String, args: Seq[Any], format: Any => String = defaultFormatter): String = {
    if (args.isEmpty) return msg
    try {
      doInterpolateVars(msg, args, format)
    } catch {
      case e: RuntimeException => throw new InterpolationException(msg, args, e)
    }
  }
  
  
  /**
   * Substitute any numeric variables in the <code>msg</code> string by using
   * the values found in the <code>args</code> sequence formatted with the
   * <code>format</code> function.
   * 
   * <p>For example:</p><code>interpolate("Hello {0}", List("World"))</code>
   * will evaluate to the string: "Hello World".</p>
   */
  def interpolateNumericVars(msg: String, args: Seq[Any], format: Any => String = defaultFormatter): String = {
    if (args.isEmpty) return msg
    try {
      doInterpolateNumeric(msg, args, format)
    } catch {
      case e: RuntimeException => throw new InterpolationException(msg, args, e)
    }
  }
  
  
  private def doInterpolateVars(msg: String, args: Seq[Any], format: Any => String = defaultFormatter) = {
    val length = msg.length
    val builder = new StringBuilder(length)
    var insideVar = false
    var argsIndex = 0
    var i = 0
      
    while (i < length) {
      val c = msg.charAt(i)
      if (c == '\\') {
        val next = msg.charAt(i+1)
        if (next == '{') {
          builder += next
          i += 2
        } else {
          builder += c
          i += 1
        }
      } else if (c == '{') {
        var end = i + 1
        while (msg.charAt(end) != '}') end += 1
        builder.append(format(args(argsIndex)))
        argsIndex += 1
        i = end + 1
      } else {
        builder += c
        i += 1
      }
    }
    builder.toString
  }
  
  private def doInterpolateNumeric(msg: String, args: Seq[Any], format: Any => String = defaultFormatter) = {
    val length = msg.length
    val builder = new StringBuilder(length)
    var insideVar = false
    var i = 0
      
    while (i < length) {
      val c = msg.charAt(i)
      if (c == '\\') {
        val next = msg.charAt(i+1)
        if (next == '{') {
          builder += next
          i += 2
        } else {
          builder += c
          i += 1
        }
      } else if (c == '{') {
        var end = i + 1
        while (msg.charAt(end) != '}') end += 1
        builder.append(format(args(msg.substring(i + 1, end).toInt - 1)))
        i = end + 1
      } else {
        builder += c
        i += 1
      }
    }
    builder.toString
  }
}

class InterpolationException(msg: String, args: Seq[Any], cause: Throwable)
  extends FrameworkException(
    "Cannot substitute variables in string: '%s' using arguments: '%s'"
      .format(msg, args.toString), cause)
