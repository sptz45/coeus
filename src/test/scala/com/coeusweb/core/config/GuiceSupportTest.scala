/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.config

import org.junit.Test
import org.junit.Assert._
import com.google.inject.Guice

class GuiceSupportTest {

  object GuiceModule extends ControllerRegistry with GuiceSupport {
    lazy val injector = Guice.createInjector(new GuiceRegistrarTest.WebModule)
  }
  
  @Test
  def register_the_controller_from_the_injector() {
    val controllers = GuiceModule.controllers.result
    assertEquals(1, controllers.size)
    assertTrue(hasExpectedType(controllers.head))
  }
  
  private def hasExpectedType(c: AnyRef) = {
    c.isInstanceOf[GuiceRegistrarTest.ExampleController]
  }
}