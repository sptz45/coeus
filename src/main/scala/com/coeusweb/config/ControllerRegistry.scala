/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.config

import com.coeusweb.Controller

/**
 * A trait to register <code>Controller</code> classes for a
 * {@code DispatcherServlet}.
 */
trait ControllerRegistry {
  /** Holds all the registered Controller classes. */
  val controllers = new Registry[Class[_ <: Controller]]
}