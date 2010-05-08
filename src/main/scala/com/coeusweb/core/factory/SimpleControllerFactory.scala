/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.factory

import com.coeusweb.Controller
import com.coeusweb.config.DispatcherConfig

/**
 * A controller factory that creates new controller instances by
 * simply calling {@link Class#newInstance}.
 */
class SimpleControllerFactory extends ControllerFactory {

  def init(config: DispatcherConfig) { }
  
  def registerClass[C <: Controller](klass: Class[C]) { }

  /**
   * Creates a new controller instance by calling the {@link Class#newInstance newInstance}
   * method on the specified controller class.
   */
  def createController[C <: Controller](klass: Class[C]): C = klass.newInstance
}