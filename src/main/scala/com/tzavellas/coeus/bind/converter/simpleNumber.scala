/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.bind.converter

import java.util.Locale

class SimpleByteConverter extends AbstractConverter[Byte] {
  def parse(text: String, locale: Locale) = filterEmpty(text, 0, _.toByte)
  def format(byte: Byte, locale: Locale) = byte.toString
}

class SimpleDoubleConverter extends AbstractConverter[Double] {
  def parse(text: String, locale: Locale) = filterEmpty(text, 0, _.toDouble)
  def format(double: Double, locale: Locale) = double.toString
}

class SimpleFloatConverter extends AbstractConverter[Float] {  
  def parse(text: String, locale: Locale) = filterEmpty(text, 0, _.toFloat)  
  def format(float: Float, locale: Locale) = float.toString
}

class SimpleIntConverter extends AbstractConverter[Int] {
  def parse(text: String, locale: Locale) = filterEmpty(text, 0, _.toInt)
  def format(int: Int, locale: Locale) = int.toString
}

class SimpleLongConverter extends AbstractConverter[Long] {  
  def parse(text: String, locale: Locale) = filterEmpty(text, 0, _.toLong)
  def format(long: Long, locale: Locale) = long.toString
}

class SimpleShortConverter extends AbstractConverter[Short] {
  def parse(text: String, locale: Locale) = filterEmpty(text, 0, _.toShort)
  def format(short: Short, locale: Locale) = short.toString
}
