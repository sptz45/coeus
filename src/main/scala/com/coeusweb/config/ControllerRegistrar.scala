/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.config

import com.coeusweb.core.ControllerRegistry

/**
 * Registers the controllers for a {@code DispatcherServlet}.  
 */
trait ControllerRegistrar {
  
  /**
   * Override to register the controllers of your application. 
   */
  def register(registry: ControllerRegistry)
}
