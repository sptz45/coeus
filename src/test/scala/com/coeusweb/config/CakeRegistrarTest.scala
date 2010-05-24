/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.config

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.Controller
import com.coeusweb.annotation.Get
import com.coeusweb.core._
import com.coeusweb.core.factory.CakeControllerFactory


class CakeRegistrarTest extends AbstractRegistrarTest {
  
  val components = CakeRegistrarTest.ComponentRegistry
  
  val config = new DispatcherConfig(null) {
    override lazy val controllerFactory = new CakeControllerFactory(components)
  }
  val registry = new ControllerRegistry(config)
  
  @Test
  def registers_all_inner_controller_classes_of_the_interfaces_of_the_specified_class() {
    CakeRegistrar.registerControllers(registry, components.getClass)
    assertViewName("/blog/list", "blog")
    assertViewName("/post/list", "posts")
  }
}

object CakeRegistrarTest{

  trait BlogComponent {
    class BlogController extends Controller {
      @Get def list() = "blog"
    }
  }
  
  trait PostComponent {
    class PostController extends Controller {
      @Get def list() = "posts"
    }
  }
  
  object ComponentRegistry extends BlogComponent with PostComponent
}