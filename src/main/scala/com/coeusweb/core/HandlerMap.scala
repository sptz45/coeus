/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import scala.collection.Set

trait HandlerMap {
    
  def isEmpty: Boolean
  
  def apply(method: Symbol): Handler
  
  def isMethodAllowed(method: Symbol): Boolean
    
  def supportedMethods: Set[Symbol]
}

object HandlerMap {
  val empty: HandlerMap = new MutableHandlerMap
}
