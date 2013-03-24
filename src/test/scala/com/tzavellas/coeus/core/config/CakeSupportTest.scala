/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core.config

import org.junit.Test
import org.junit.Assert._

class CakeSupportTest {

  object CakeModule extends ControllerRegistry with CakeSupport {
    def components = Seq(CakeRegistrarTest.ComponentRegistry)
  }
  
  @Test
  def register_the_controller_from_the_component_registry() {
    val controllers = CakeModule.controllers.result
    assertEquals(1, controllers.size)
    assertTrue(hasExpectedType(controllers.head))
  }
  
  private def hasExpectedType(c: AnyRef) = {
    c.isInstanceOf[CakeRegistrarTest.BlogComponent#BlogController]
  }
}