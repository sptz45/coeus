/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core

import java.lang.reflect.Method

private class HandlerMapping(
  val path: String,
  val httpMethod: Symbol,
  val controllerMethod: Method)
