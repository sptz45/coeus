/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core

import scala.collection.Map

/**
 * Finds the {@code Handler} to handle a given request.
 */
trait RequestResolver {
  
  /**
   * Register the given {@code Hander} to handle requests for the specified
   * path and HTTP method.
   * 
   * @param method  the HTTP method of the request
   * @param path    the path (requestUri) of the request
   * @param handler the {@code Handler} to register
   */
  def register(method: Symbol, path: String, handler: Handler)
  
  /**
   * Finds the {@code Handler}s that can handle a request for the specified
   * path.
   * 
   * @param path the path (requestUri) of the request
   * 
   * @return a pair that contains the {@code HandlerMap} with the handlers for
   *         the specified path and a map with any path variables.
   */
  def resolve(path: String): (HandlerMap, Map[String, String])
}



