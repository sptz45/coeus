/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.config

import org.junit.Test
import org.junit.Assert._
import com.google.inject.Guice
import com.coeusweb.Controller
import com.coeusweb.core._
import com.coeusweb.core.factory.GuiceControllerFactory

class GuiceRegistrarTest {
  
  import GuiceRegistrarTest._
  
  val injector = Guice.createInjector(new WebModule)
  val registry = new ControllerRegistry { }

  @Test
  def registers_controllers_from_guice_injector() {
    GuiceRegistrar.registerControllers(registry, injector)
    assertTrue(registry.controllers.contains(classOf[GuiceController]))
  }
}

object GuiceRegistrarTest {
  import com.google.inject.AbstractModule
  
  class WebModule extends AbstractModule {
    def configure() {
      bind(classOf[GuiceController])
    }
  }
  
  class GuiceController extends Controller
}
