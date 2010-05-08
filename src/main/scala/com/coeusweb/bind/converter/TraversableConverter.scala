/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import java.util.Locale
import scala.collection.generic.GenericCompanion
import scala.collection.mutable.Builder
import com.coeusweb.bind.Converter
  
class TraversableConverter[A, Coll[B] <: Traversable[B]](
  converter: Converter[A],
  companion: GenericCompanion[Coll],
  separator: String = ",")
    extends Converter[Coll[A]] {

  def parse(text: String, locale: Locale): Coll[A] = {
    val builder: Builder[A, Coll[A]] = companion.newBuilder[A]
    for (i <- text.split(separator)) {
      builder += converter.parse(i.trim, locale)
    }
    builder.result
  }
    
  def format(collection: Coll[A], locale: Locale): String = {
    collection.map(converter.format(_, locale)).mkString(separator + " ")
  }
}
