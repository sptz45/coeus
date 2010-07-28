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
  
  object registry extends ControllerRegistry

  @Test
  def registers_controllers_from_guice_injector() {
    val injector = Guice.createInjector(new WebModule)
    GuiceRegistrar.registerControllers(registry, injector)
    assertTrue(registry.controllers.result.contains(classOf[ExampleController]))
  }
  
  @Test//(expected=classOf[FrameworkException])
  def detects_non_no_scope_controllers() {
    val injector = Guice.createInjector(new ErroneousWebModule)
    GuiceRegistrar.registerControllers(registry, injector)
  }
}

object GuiceRegistrarTest {
  import com.google.inject.AbstractModule
  
  class WebModule extends AbstractModule {
    def configure() {
      bind(classOf[ExampleController])
    }
  }
  
  class ErroneousWebModule extends AbstractModule {
    def configure() {
      bind(classOf[ExampleController]).asEagerSingleton()
    }
  }
  
  class ExampleController extends Controller
}
