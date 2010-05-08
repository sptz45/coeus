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
 * The result of finding a handler via a <code>RequestResolver</code>.
 * 
 * @see RequestResolver
 */
sealed trait Resolution

/**
 * Returned by <code>RequestResolver</code> when a handler could not get
 * found for a given path.
 */
object HandlerNotFound extends Resolution

/**
 * Returned by <code>RequestResolver</code> when the handler found for
 * the given path does not support the request's HTTP method.
 */
object MethodNotAllowed extends Resolution

/**
 * Returned by <code>RequestResolver</code> when a handler is found for
 * a given request.
 * 
 * @param handler the handler to handle the request
 * @param pathVariables a map with any variables that were extracted from the request URI
 */
final case class SuccessfulResolution(
  handler: Handler[Controller],
  pathVariables: Map[String, String]) extends Resolution
