/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.factory

import java.lang.reflect.Constructor
import com.coeusweb.Controller
import com.coeusweb.core.FrameworkException

class CakeControllerFactory(val componentRegistry: AnyRef) extends ControllerFactory {

  private[this] var cache = Map[Class[_], Constructor[_]]()

  def controllerRegistered[C <: Controller](controllerClass: Class[C]) {
    try {
      val constructor = controllerClass.getConstructors.apply(0) 
      require(constructor.getParameterTypes.length == 1)
      cache = cache + (controllerClass -> constructor)
    } catch {
      case cause: Exception => throw new FrameworkException(
        "Failed to register controller with class: %s. The the class must have a public no-arg construcor."
          .format(controllerClass.getName),
        cause)
    }
  }
  
  def createController[C <: Controller](klass: Class[C]): C = {
    try {
      cache(klass).newInstance(componentRegistry).asInstanceOf[C]
    } catch {
      case e: NoSuchElementException =>
        throw new FrameworkException(
          "Could not instantiate a controller with class: %s because the class hasn't been registered"
            .format(klass.getName))
    }
  }
}