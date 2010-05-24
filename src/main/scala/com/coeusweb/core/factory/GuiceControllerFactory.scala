/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.factory

import com.google.inject.Injector
import com.coeusweb.Controller

class GuiceControllerFactory(injector: Injector) extends ControllerFactory {
  
  def controllerRegistered[C <: Controller](klass: Class[C]) { }

  def createController[C <: Controller](klass: Class[C]): C = injector.getInstance(klass)
}