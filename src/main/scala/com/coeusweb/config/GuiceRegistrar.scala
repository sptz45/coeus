/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.config

import scala.collection.JavaConversions._
import com.google.inject.Injector
import com.coeusweb.Controller

/**
 * Register all the {@code Controller} classes from the bindings in a Guice
 * injector.
 */
object GuiceRegistrar {
  
  /**
   * Register all the {@code Controller} classes from the specified Guice
   * {@code Injector}. 
   * 
   * <p>Should be used in conjunction with the {@code GuiceControllerFactory}.</p>
   */
  def registerControllers(registry: ControllerRegistry, injector: Injector) {
    injector.getBindings
            .keySet
            .map(k => k.getTypeLiteral.getRawType)
            .filter(c => classOf[Controller].isAssignableFrom(c))
            .foreach(c => registry.controllers += c.asInstanceOf[Class[Controller]])
  }
}