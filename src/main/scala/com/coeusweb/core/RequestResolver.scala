/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import scala.collection.Map
import com.coeusweb.Controller

/**
 * Finds the <code>Handler</code> to handle a given request.
 */
trait RequestResolver {
  
  /**
   * Register the given <code>Hander</code> to handle requests for the specified
   * path and HTTP method.
   * 
   * @param path the path (requestUri) of the request
   * @param method the HTTP method of the request
   * @param handler the handler to register
   */
  def register(path: String, method: Symbol, handler: Handler[Controller])
  
  /**
   * Finds the <code>Handler</code> to handle the request for the specified
   * path and method.
   * 
   * @param path the path (requestUri) of the request
   * @param method the HTTP method of the request
   * 
   * @return <code>HandlerNotFound</code> if a <code>Handler</code> does not exist
   *         for the specified path, <code>MethodNotAllowed</code> if a <code>Handler</code>
   *         exists but not for the specified method, else a <code>SuccessfulResolution<</code>
   */
  def resolve(path: String, method: Symbol): Resolution
}



