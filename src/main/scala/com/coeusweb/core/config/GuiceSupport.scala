/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.config

import com.google.inject.Injector

/**
 * Performs all the necessary setup to allow a Guice <code>Injector</code>
 * to instantiate the controllers of this web module.
 * 
 * <p>This trait registers all the <code>Controller</code> classes found
 * in the <code>Injector</code> in the module's controller registry. The
 * controller's must have the {@code Singleton} scope.</p>
 * 
 * @see <a href="http://code.google.com/p/google-guice/">Guice</a>
 */
trait GuiceSupport {

  this: ControllerRegistry =>

  /** The injector to use for creating the Controllers. */
  def injector: Injector

  GuiceRegistrar.registerControllers(this, injector)
}