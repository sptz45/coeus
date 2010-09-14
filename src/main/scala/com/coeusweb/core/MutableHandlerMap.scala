/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core


private class MutableHandlerMap extends HandlerMap {

  @volatile
  private var handlers = Map[Symbol, Handler]()

  def hasHandlers = !handlers.isEmpty

  def isEmpty = handlers.isEmpty
  
  def isMethodAllowed(method: Symbol) = handlers.contains(method)
  
  def supportedMethods = handlers.keySet.map(_.name)

  def apply(method: Symbol) = handlers(method)

  def put(method: Symbol, handler: Handler) {
    handlers = handlers + (method -> handler)
  }

  def putAll(source: MutableHandlerMap) {
    handlers = handlers ++ source.handlers
  }

  def addMissingHandlers(source: MutableHandlerMap) {
    for (handler <- source.handlers if ! handlers.contains(handler._1))
      handlers = handlers + handler
  }

  override def toString = handlers.toString
}