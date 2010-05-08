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
 * A converter for Arrays. 
 */
class ArrayConverter[A]
  (converter: Converter[A], separator: String = ",")
  (implicit m: ClassManifest[A])
    extends Converter[Array[A]] {

  def parse(text: String, locale: Locale): Array[A] = {
    val input = text.split(separator)
    val result = new Array[A](input.length)
    var i = 0
    while (i < input.length) {
      result(i) = converter.parse(input(i), locale)
      i += 1
    }
    result
  }
    
  def format(collection: Array[A], locale: Locale): String = {
    collection.map(converter.format(_, locale)).mkString(separator + " ")
  }
}
