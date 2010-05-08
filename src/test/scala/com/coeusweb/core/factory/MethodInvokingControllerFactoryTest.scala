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


class MethodInvokingControllerFactoryTest {
  import MethodInvokingControllerFactoryTest._
  
  val factory = new MethodInvokingControllerFactory(ComponentRegistry)

  @Test
  def register_and_create_a_controller() {
    factory.registerClass(classOf[BlogController])
    assertNotNull(factory.createController(classOf[BlogController]))
  }
  
  @Test(expected=classOf[FrameworkException])
  def exception_when_no_method_found_matching_the_controllers_class_name() {
    factory.registerClass(classOf[PostController])
  }
  
  @Test(expected=classOf[FrameworkException])
  def exception_when_the_controller_class_has_not_been_registered() {
    factory.createController(classOf[PostController])
  }
}

object MethodInvokingControllerFactoryTest {
  
  class BlogController extends Controller
  class PostController extends Controller
  
  object ComponentRegistry {
    def blogController = new BlogController
  }
}