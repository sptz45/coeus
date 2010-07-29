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


class SingleRegistryCakeControllerFactoryTest extends CakeControllerFactoryTest {
  import CakeControllerFactoryTest._
  val cake = new CakeControllerFactory(ComponentRegistry)
}


class MultipleRegistriesCakeControllerFactoryTest extends CakeControllerFactoryTest {
  import CakeControllerFactoryTest._
  val cake = new CakeControllerFactory(DummyRegistry, ComponentRegistry)
}


abstract class CakeControllerFactoryTest {
  import CakeControllerFactoryTest._
  
  val cake: CakeControllerFactory

  @Test(expected=classOf[IllegalArgumentException])
  def cannot_construct_cake_factory_without_registry() {
    new CakeControllerFactory()
  }
  
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
  
  @Test(expected=classOf[FrameworkException])
  def exception_when_no_public_constructor() {
    cake.controllerRegistered(classOf[ErrorsComponent#PrivateConstructorController])
  }
  
  @Test(expected=classOf[FrameworkException])
  def exception_when_constuctor_takes_parameters() {
    cake.controllerRegistered(classOf[ErrorsComponent#ConstructorWithArgsController])
  }
}


object CakeControllerFactoryTest {
  
  trait BlogComponent {
    class BlogController extends Controller
  }
  
  trait PostComponent {
    class PostController(post: String) extends Controller
  }
  
  trait ErrorsComponent {
    class PrivateConstructorController private () extends Controller
    class ConstructorWithArgsController(arg: Int) extends Controller
  }
  
  object ComponentRegistry extends BlogComponent
                              with PostComponent
  object DummyRegistry
}