/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.factory

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.Controller

class GuiceControllerFactoryTest {
  
  import GuiceControllerFactoryTest._

  val factory = new GuiceControllerFactory(new AppModule)
  
  @Test
  def register_a_controller_and_assert_that_it_is_injected_after_creation() {
    factory.registerClass(classOf[GuiceController])
    val c = factory.createController(classOf[GuiceController]) 
    assertTrue(c.isInstanceOf[GuiceController])
    assertNotNull(c.injected)
  }
}

object GuiceControllerFactoryTest {
  import com.google.inject.AbstractModule
  import com.google.inject.Inject
  
  class AppModule extends AbstractModule {
    def configure() {
      bind(classOf[Dependency]).to(classOf[Implementation])
    }
  }
  
  class GuiceController @Inject() (val injected: Dependency) extends Controller
  trait Dependency
  class Implementation extends Dependency
}