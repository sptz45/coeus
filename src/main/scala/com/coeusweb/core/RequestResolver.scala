/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import com.coeusweb.mvc.controller.Controller

/**
 * Finds the {@code Handler} to handle a given request.
 */
trait RequestResolver {
  
  /**
   * Register the given {@code Hander} to handle requests for the specified
   * path and HTTP method.
   * 
   * @param path    the path (requestUri) of the request
   * @param method  the HTTP method of the request
   * @param handler the {@code Handler} to register
   */
  def register(path: String, method: Symbol, handler: Handler)
  
  /**
   * Finds the {@code Handler} to handle the request for the specified path
   * and method.
   * 
   * @param path   the path (requestUri) of the request
   * @param method the HTTP method of the request
   * 
   * @return {@code HandlerNotFound} if a {@code Handler} does not exist for
   *         the specified path, {@code MethodNotAllowed} if a {@code Handler}
   *         exists but not for the specified method, else a
   *         {@code SuccessfulResolution}.
   */
  def resolve(path: String, method: Symbol): Resolution
  
  def options(path: String): Traversable[Symbol]
}



