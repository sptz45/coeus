/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.factory

import com.coeusweb.mvc.controller.Controller

/**
 * Creates the {@code Controller}'s instance in each request.
 * 
 * @see Controller
 */
trait ControllerFactory {

  /**
   * This methods gets called by the framework when a {@code Controller}
   * class get registered during application initialization.
   * 
   * @param controllerClass the class of the {@code Controller} that got
   *        registered.
   */
  def controllerRegistered[C <: Controller](controllerClass: Class[C])
  
  /**
   * Create a new instance of the {@code Controller} with the specified type.
   */
  def createController[C <: Controller](klass: Class[C]): C
}