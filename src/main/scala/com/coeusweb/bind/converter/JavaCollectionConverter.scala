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
 */
class JavaCollectionConverter[Coll[Elem] <: Collection[Elem], Elem](
  converter: Converter[Elem],
  newCollection: Int => Coll[Elem],
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

object JavaCollectionConverter {
  
  def forArrayList[E](converter: Converter[E], separator: String = ",", appendSpace: Boolean = true) = {
    new JavaCollectionConverter[ArrayList, E](converter, { size: Int => new ArrayList[E](size) }, separator, appendSpace)
  }
    
  def forLinkedList[E](converter: Converter[E], separator: String = ",", appendSpace: Boolean = true) = {
    new JavaCollectionConverter[LinkedList, E](converter, { size: Int => new LinkedList[E]}, separator, appendSpace)
  }
  
  def forHashSet[E](converter: Converter[E], separator: String = ",", appendSpace: Boolean = true) = {
    new JavaCollectionConverter[HashSet, E](converter, { size: Int => new HashSet[E](size)}, separator, appendSpace)
  }
}