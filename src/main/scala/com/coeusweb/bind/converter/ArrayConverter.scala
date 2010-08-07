/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import java.util.Locale
import scala.collection.mutable.ArrayBuilder
import com.coeusweb.bind.Converter

/**
 * A {@code Converter} for Arrays.
 * 
 * @param A           the type of the array's elements
 *
 * @param converter   the {@code Converter} for the array's elements
 * @param separator   the string that separates the array elements (default is ",")
 * @param appendSpace whether to append a space character after the separator when
 *                    formatting the array (default is {@code true})
 */
class ArrayConverter[A](
  converter: Converter[A],
  separator: String = ",",
  appendSpace: Boolean = true)(implicit m: ClassManifest[A])
    extends Converter[Array[A]] {

  def parse(text: String, locale: Locale): Array[A] = {
    if ((text eq null) || text.trim.isEmpty)
      return Array()
    
    val input = text.split(separator).map(_.trim).filterNot(_.isEmpty)
    val result = new Array[A](input.length)
    var i = 0
    while (i < input.length) {
      result(i) = converter.parse(input(i), locale)
      i += 1
    }
    result
  }
    
  def format(array: Array[A], locale: Locale): String = {
    if (array eq null) 
      return ""
    
    val formatSeparator =
      if (appendSpace) separator + " " else separator
    
    array.map(converter.format(_, locale)).mkString(formatSeparator)
  }
}
