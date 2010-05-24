/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.factory

import org.junit.{ Test, Ignore }
import org.junit.Assert._
import com.coeusweb.Controller
import com.coeusweb.core.FrameworkException

class CakeControllerFactoryTest {
  import CakeControllerFactoryTest._
  
  val cake = new CakeControllerFactory(ComponentRegistry)

  @Test
  def register_and_create_a_controller() {
    cake.controllerRegistered(classOf[BlogComponent#BlogController])
    assertNotNull(cake.createController(classOf[BlogComponent#BlogController]))
  }
  
  @Test(expected=classOf[FrameworkException])
  def exception_when_no_class_does_not_have_a_no_arg_constructor() {
    cake.controllerRegistered(classOf[PostComponent#PostController])
  }
  
  @Test(expected=classOf[FrameworkException])
  def exception_when_the_controller_class_has_not_been_registered() {
    cake.createController(classOf[BlogComponent#BlogController])
  }
}

object CakeControllerFactoryTest {
  
  trait BlogComponent {
    class BlogController extends Controller
  }
  
  trait PostComponent {
    class PostController(post: String) extends Controller
  }
  
  object ComponentRegistry extends BlogComponent
                              with PostComponent
}