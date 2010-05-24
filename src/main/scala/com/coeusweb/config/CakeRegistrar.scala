/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.config

import com.coeusweb.Controller
import com.coeusweb.core.ControllerRegistry

/**
 * Register all the {@code Controller} classes found in a Cake component
 * registry.
 */
object CakeRegistrar {
  
  /**
   * Register all the inner classes of the implemented interfaces of the specified
   * class that extend the {@code Controller} abstract class.
   * 
   * <p>Should be used in conjunction with the {@code CakeControllerFactory}.</p>
   */
  def registerControllers(registry: ControllerRegistry, containerClass: Class[_]) {
    containerClass.getInterfaces
                  .flatMap(_.getDeclaredClasses)
                  .collect({ case c if classOf[Controller].isAssignableFrom(c) => c.asInstanceOf[Class[Controller]]})
                  .foreach(c => registry.register(c))
  }
}
