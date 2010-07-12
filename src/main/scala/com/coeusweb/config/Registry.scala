/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.config

import scala.collection.mutable.ListBuffer

final class Registry[T] {
  
  private val objects = new ListBuffer[T]
  
  /** Register the specified object. */
  def +=(fo: T) { objects += fo }
  
  /**
   * Return a sequence with all the registered objects preserving
   * the order in which the objects were registered.
   */
  def get: Seq[T] = objects.result
}



