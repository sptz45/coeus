/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import java.util._
import com.coeusweb.bind.Converter

/**
 * A {@code Converter} for Java collections.
 *
 * @param Coll        the type of the collection
 * @param Elem        the type of the collection's elements
 *
 * @param converter   the {@code Converter} for the collection's elements
 * @param separator   the string that separates the collection elements (default is ",")
 * @param appendSpace whether to append a space character after the separator when
 *                    formatting the collection (default is {@code true})
 * 
 * @see TraversableConverter
 */
class CollectionConverter[Coll[Elem] <: Collection[Elem], Elem](
  newCollection: Int => Coll[Elem],
  converter: Converter[Elem],
  separator: String = ",",
  appendSpace: Boolean = true)
    extends Converter[Coll[Elem]] {

  def parse(text: String, locale: Locale): Coll[Elem] = {
    if ((text eq null) || text.trim.isEmpty)
      return newCollection(0)
      
    val input = text.split(separator).map(_.trim).filterNot(_.isEmpty)
    val result = newCollection(input.size)
    var i = 0
    while (i < input.length) {
      result.add(converter.parse(input(i), locale))
      i += 1
    }
    result
  }
    
  def format(collection: Coll[Elem], locale: Locale): String = {
    if ((collection eq null) || collection.isEmpty)
      return ""
      
    val formatSeparator =
      if (appendSpace) separator + " " else separator
      
    val builder = new java.lang.StringBuilder
    val iter = collection.iterator
    while (iter.hasNext) {
      builder.append(converter.format(iter.next, locale))
      builder.append(formatSeparator)
    }
    builder.substring(0, builder.length - formatSeparator.length)
  }
}

/**
 * Factory methods for creating converters for Java collections.
 */
object CollectionConverter {
  
  /**
   * Creates a {@code Converter} for {@see java.util.ArrayList}.
   *
   * @param E           the type of the ArrayList's elements
   *
   * @param converter   the {@code Converter} for the ArrayList's elements
   * @param separator   the string that separates the ArrayList elements (default is ",")
   * @param appendSpace whether to append a space character after the separator when
   *                    formatting the ArrayList (default is {@code true})
   */
  def forArrayList[E](converter: Converter[E], separator: String = ",", appendSpace: Boolean = true) = {
    new CollectionConverter[ArrayList, E]({ size: Int => new ArrayList[E](size) }, converter, separator, appendSpace)
  }
  
  /**
   * Creates a {@code Converter} for {@link java.util.LinkedList}.
   *
   * @param E           the type of the LinkedList's elements
   *
   * @param converter   the {@code Converter} for the LinkedList's elements
   * @param separator   the string that separates the LinkedList elements (default is ",")
   * @param appendSpace whether to append a space character after the separator when
   *                    formatting the LinkedList (default is {@code true})
   */
  def forLinkedList[E](converter: Converter[E], separator: String = ",", appendSpace: Boolean = true) = {
    new CollectionConverter[LinkedList, E]({ size: Int => new LinkedList[E]}, converter, separator, appendSpace)
  }
  
  /**
   * Creates a {@code Converter} for {@link java.util.HashSet}.
   *
   * @param E           the type of the HashSet elements
   *
   * @param converter   the {@code Converter} for the HashSet's elements
   * @param separator   the string that separates the HashSet elements (default is ",")
   * @param appendSpace whether to append a space character after the separator when
   *                    formatting the HashSet (default is {@code true})
   */
  def forHashSet[E](converter: Converter[E], separator: String = ",", appendSpace: Boolean = true) = {
    new CollectionConverter[HashSet, E]({ size: Int => new HashSet[E](size)}, converter, separator, appendSpace)
  }
}