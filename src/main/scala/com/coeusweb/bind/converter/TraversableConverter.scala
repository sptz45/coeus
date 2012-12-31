/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import language.higherKinds

import java.util.Locale
import scala.collection.mutable.Builder
import com.coeusweb.bind.Converter

/**
 * A {@code Converter} for Scala collections.
 *
 * @param Coll        the type of the collection
 * @param Elem        the type of the collection's elements
 *
 * @param newBuilder  creates a builder for the collection
 * @param converter   the {@code Converter} for the collection's elements
 * @param separator   the string that separates the collection elements (default is ",")
 * @param appendSpace whether to append a space character after the separator when
 *                    formatting the collection (default is {@code true})
 * 
 * @see CollectionConverter
 */
class TraversableConverter[Coll[Elem] <: Traversable[Elem], Elem](
  newBuilder: Int => Builder[Elem, Coll[Elem]],
  converter: Converter[Elem],
  separator: String = ",",
  appendSpace: Boolean = true) extends Converter[Coll[Elem]] {
  
  def parse(text: String, locale: Locale): Coll[Elem] = {
    if ((text eq null) || text.trim.isEmpty)
      return newBuilder(0).result
      
    val input = text.split(separator).map(_.trim).filterNot(_.isEmpty)
    val builder = newBuilder(input.size)
    for (segment <- input) {
      builder += converter.parse(segment, locale)
    }
    builder.result
  }
  
  def format(collection: Coll[Elem], locale: Locale): String = {
    if ((collection eq null) || collection.isEmpty)
      return ""
      
    val formatSeparator =
      if (appendSpace) separator + " " else separator
      
    val builder = new java.lang.StringBuilder
    for (elem <- collection) {
      builder.append(converter.format(elem, locale))
      builder.append(formatSeparator)
    }
    builder.substring(0, builder.length - formatSeparator.length)
  }
}

/**
 * Factory methods for creating converters for Scala collections.
 */
object TraversableConverter {

  import scala.collection.generic.GenericCompanion
  
  /**
   * Creates a {@code Converter} from a {@link GenericCompanion} of a Scala collection.
   *
   * @param Coll        the type of the collection
   * @param Elem        the type of the collection's elements
   *
   * @param companion   the companion object of the collection
   * @param converter   the {@code Converter} for the collection's elements
   * @param separator   the string that separates the collection elements (default is ",")
   * @param appendSpace whether to append a space character after the separator when
   *                    formatting the collection (default is {@code true})
   */
  def fromCompanion[Coll[Elem] <: Traversable[Elem], Elem](
    companion: GenericCompanion[Coll],
    converter: Converter[Elem],
    separator: String = ",",
    appendSpace: Boolean = true) = {
    
    new TraversableConverter[Coll, Elem](
      { size => val b = companion.newBuilder[Elem]; b.sizeHint(size); b },
      converter, separator, appendSpace)
  }
}