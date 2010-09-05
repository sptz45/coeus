/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.util.internal

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map
import java.util.{ Map => JMap }

class MultiMap[A, B] extends Iterable[(A, Seq[B])] {
  
  private[this] val map: Map[A, ArrayBuffer[B]] = Map()
  
  def iterator = map.iterator

  def add(key: A, value: B) {
    if (!map.contains(key)) {
      map.put(key, new ArrayBuffer)
    }
    map(key) += value
  }
  
  def get(key: A): Seq[B] = map.get(key).getOrElse(MultiMap.EMPTY_SEQ)
  
  def getFirst(key: A): Option[B] = map.get(key).flatMap(_.headOption)
}

private object MultiMap {
 
  val EMPTY_SEQ = Seq()
}