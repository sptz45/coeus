/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.factory

import com.google.inject.Injector
import com.coeusweb.Controller

/**
 * Creates {@code Controller} instances from a Guice {@code Injector}.
 * 
 * <p>The controller bindings (in the Guice injector) <strong>must be</strong>
 * configured with <em>no scope</em> to enable this factory to create a new
 * controller instance in each request.</p>
 *
 * <p>Should be used in conjunction with
 * {@link com.coeusweb.config.GuiceRegistrar GuiceRegistrar}.</p>
 *
 * @param injector the {@code Injector} to use for creating the
 *        {@code Controller} instances
 * 
 * @see Controller
 * @see <a href="http://code.google.com/p/google-guice/">Guice</a>
 */
class GuiceControllerFactory(injector: Injector) extends ControllerFactory {
  
  def controllerRegistered[C <: Controller](klass: Class[C]) { }

  def createController[C <: Controller](klass: Class[C]): C = injector.getInstance(klass)
}