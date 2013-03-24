/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core.config

import com.tzavellas.coeus.FrameworkException
import java.lang.reflect.Constructor
import com.tzavellas.coeus.mvc.controller.Controller

/**
 * Instantiate and register all the {@code Controller} classes found in a Cake
 * component registry.
 */
private object CakeRegistrar {
  
  /**
   * Register all the inner classes of the implemented interfaces of the
   * specified class that extend the {@code Controller} abstract class.
   * 
   * @param registry   where to register the controller classes.
   * @param components the Cake "component registry".
   */
  def registerControllers(registry: ControllerRegistry, components: AnyRef) {
    components.getClass.getInterfaces
      .flatMap(_.getDeclaredClasses)
      .collect({
        case c if classOf[Controller].isAssignableFrom(c) => 
          c.asInstanceOf[Class[Controller]]
       })
      .foreach(c => registry.controllers += createController(c, components))
  }
  
  
  private def createController(cc: Class[Controller], registry: AnyRef) = {
    try {
      /* Constructors of inner classes take the outer class as the first
       * parameter. For that reason we retrieve the constructor that takes
       * one parameter instead of zero. */
      val constructor = cc.getConstructors.apply(0)
      constructor.newInstance(registry).asInstanceOf[Controller]
    } catch {
      case cause: Exception => throw new FrameworkException(
        ("Failed to register controller with class: %s. The the class must " +
        "be an inner class with a public no-arg constructor.")
          .format(cc.getName), cause)
    }
  }
}
