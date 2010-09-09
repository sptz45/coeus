/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.config

import scala.collection.mutable.{ Builder, ArrayBuffer }
import com.coeusweb.mvc.controller.Controller

/**
 * A trait to register <code>Controller</code> classes for a
 * {@code DispatcherServlet}.
 */
trait ControllerRegistry {
  
  type ControllerClass = Class[_ <: Controller] 
  
  /** Holds all the registered Controller classes. */
  val controllers: Builder[ControllerClass, Seq[ControllerClass]] = new ArrayBuffer(50)
}
