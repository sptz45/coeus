/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.config

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.Controller
import com.coeusweb.core._
import com.coeusweb.core.factory.CakeControllerFactory


class CakeRegistrarTest {
  import CakeRegistrarTest._
  
  val components = CakeRegistrarTest.ComponentRegistry
  val registry = new ControllerRegistry { }
  
  @Test
  def registers_all_inner_controller_classes_of_the_interfaces_of_the_specified_class() {
    CakeRegistrar.registerControllers(registry, components.getClass)
    val controllers = registry.controllers.result
    assertTrue(controllers.contains(classOf[BlogComponent#BlogController]))
    assertTrue(controllers.contains(classOf[PostComponent#PostController]))
  }
}

object CakeRegistrarTest{

  trait BlogComponent {
    class BlogController extends Controller
  }
  
  trait PostComponent {
    class PostController extends Controller
  }
  
  object ComponentRegistry extends BlogComponent with PostComponent
}