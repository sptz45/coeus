/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.factory

import org.junit.Test
import org.junit.Assert._
import com.google.inject.Guice
import com.coeusweb.Controller

class GuiceControllerFactoryTest {
  
  import GuiceControllerFactoryTest._
  
  @Test
  def create_a_controller_from_a_guice_injector() {
    val factory = new GuiceControllerFactory(Guice.createInjector(new WebModule))
    val c = factory.createController(classOf[GuiceController]) 
    assertTrue(c.isInstanceOf[GuiceController])
    assertNotNull(c.injected)
  }
}

object GuiceControllerFactoryTest {
  import com.google.inject.AbstractModule
  import com.google.inject.Inject
  
  class WebModule extends AbstractModule {
    def configure() {
      bind(classOf[Dependency]).to(classOf[Implementation])
      bind(classOf[GuiceController])
    }
  }
  
  class GuiceController @Inject() (val injected: Dependency) extends Controller
  trait Dependency
  class Implementation extends Dependency
}
