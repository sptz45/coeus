/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.config

import com.google.inject.Injector
import com.coeusweb.core.factory.GuiceControllerFactory

/**
 * Performs all the necessary setup to allow a Guice <code>Injector</code>
 * to instantiate the controllers of this web module.
 * 
 * <p>This trait registers all the <code>Controller</code> classes found
 * in the <code>Injector</code> in the module's controller registry and
 * sets the module's <code>controllerFactory</code> to
 * <code>GuiceControllerFactory</code>. 
 *
 * @see Controller
 * @see <a href="http://code.google.com/p/google-guice/">Guice</a>
 */
trait GuiceSupport {

  this: WebModule =>

  /**
   * The injector to use for creating the Controllers.
   * 
   * <p>This method must always return the same <code>Injector</code> instance.
   * To override correctly use a <strong>lazy val</strong>. Overriding with
   * <em>val</em> may lead to <code>NullPointerException</code>.</p>
   */
  def injector: Injector
  
  controllerFactory = new GuiceControllerFactory(injector)
  GuiceRegistrar.registerControllers(this, injector)
}