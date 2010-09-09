/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.config

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.FrameworkException
import com.coeusweb.mvc.controller.Controller
import com.coeusweb.core._


class CakeRegistrarTest {
  import CakeRegistrarTest._
  
  val components = CakeRegistrarTest.ComponentRegistry
  val registry = new ControllerRegistry { }
  
  @Test
  def registers_all_inner_controller_classes_of_the_interfaces_of_the_specified_class() {
    CakeRegistrar.registerControllers(registry, components)
    val controllers = registry.controllers.result
    assertTrue(controllers.exists(_.getClass == classOf[BlogComponent#BlogController]))
  }
  
  @Test(expected=classOf[FrameworkException])
  def exception_when_no_public_constructor() {
    CakeRegistrar.registerControllers(registry, ErrorPrivateComponentRegistry)
  }
  
  @Test(expected=classOf[FrameworkException])
  def exception_when_constuctor_takes_parameters() {
    CakeRegistrar.registerControllers(registry, ErrorsArgsComponentRegistry)
  }
}

object CakeRegistrarTest{

  trait BlogComponent {
    class BlogController extends Controller
  }
  
  object ComponentRegistry extends BlogComponent
  
  trait ErrorPrivateComponent {
    class PrivateConstructorController private () extends Controller
  }
  
  object ErrorPrivateComponentRegistry extends ErrorPrivateComponent
  
  trait ErrorsArgsComponent {
    class ConstructorWithArgsController(arg: Int) extends Controller
  }
  
  object ErrorsArgsComponentRegistry extends ErrorsArgsComponent  
}