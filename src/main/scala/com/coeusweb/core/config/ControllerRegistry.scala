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
 * A trait to register the {@code Controller} instances for a
 * {@code DispatcherServlet}.
 */
trait ControllerRegistry { 
  
  /** Holds all the registered Controller classes. */
  val controllers: Builder[Controller, Seq[Controller]] = new ArrayBuffer(50)
}
