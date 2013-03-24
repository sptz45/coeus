/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core.config

import org.junit.Test
import org.junit.Assert._
import com.google.inject.Guice
import com.tzavellas.coeus.FrameworkException
import com.tzavellas.coeus.mvc.controller.Controller

class GuiceRegistrarTest {
  
  import GuiceRegistrarTest._
  
  object registry extends ControllerRegistry

  @Test
  def registers_controllers_from_guice_injector() {
    val injector = Guice.createInjector(new WebModule)
    GuiceRegistrar.registerControllers(registry, injector)
    val controllers = registry.controllers.result
    assertTrue(controllers.exists(_.getClass == classOf[ExampleController]))
  }
  
  @Test(expected=classOf[FrameworkException])
  def detects_non_no_scope_controllers() {
    val injector = Guice.createInjector(new ErroneousWebModule)
    GuiceRegistrar.registerControllers(registry, injector)
  }
}

object GuiceRegistrarTest {
  import com.google.inject.AbstractModule
  
  class WebModule extends AbstractModule {
    def configure() {
      bind(classOf[ExampleController]).asEagerSingleton()
    }
  }
  
  class ErroneousWebModule extends AbstractModule {
    def configure() {
      bind(classOf[ExampleController])
    }
  }
  
  class ExampleController extends Controller
}
