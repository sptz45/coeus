/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.Controller
import com.coeusweb.annotation.Get
import com.coeusweb.config.DispatcherConfig
import com.coeusweb.core.factory._
import com.coeusweb.interceptor.ThreadLocalRequestInterceptor
import com.coeusweb.scope.support.FlashScopeInterceptor

class ControllerRegistryTest {
  import ControllerRegistryTest._

  val factory = new TestFactory
  val config = new CustomConfig(factory)
  val registry = new ControllerRegistry(config)
  
  @Test
  def by_default_interceptors_contains_the_flash_scope_and_thead_local_interceptors() {
    val interceptors = registry.interceptors.result
    assertEquals(2, interceptors.length)
    assertTrue(interceptors.exists(_.isInstanceOf[FlashScopeInterceptor]))
    assertTrue(interceptors.exists(_.isInstanceOf[ThreadLocalRequestInterceptor]))
  }
  
  @Test
  def register_a_controller() {
    registry.register[ProjectController]
    assertHandlerFound("/project/list")
  }
  
  @Test
  def a_controller_class_does_not_get_registered_if_it_is_abstract() {
    registry.register[AbstractController]
    assertNoHanderFound("/abstract/index")
  }
  
  @Test
  def when_a_controller_gets_registered_a_callback_is_made_into_the_controller_facory() {
    registry.register[ProjectController]
    assertEquals(classOf[ProjectController], factory.controller)
  }
  
  def assertHandlerFound(path: String, m: Symbol = 'GET) {
    config.requestResolver.resolve(path, m) match {
      case HandlerNotFound => fail("No handler found for path %s".format(path))
      case MethodNotAllowed => fail("Method %s not allowed for path %s".format(m.toString, path))
      case _ => ()
    }
  }

  def assertNoHanderFound(path: String) {
    assertEquals(HandlerNotFound, config.requestResolver.resolve(path, 'GET)) 
  }
}

object ControllerRegistryTest {
  
  abstract class AbstractController extends Controller {
    @Get def index() = "index"
  }
  
  class ProjectController extends Controller {
    @Get def list() = "projects"
  }
  
  class CustomConfig(factory: ControllerFactory) extends DispatcherConfig(null) {
    override lazy val controllerFactory = factory 
  }
  
  class TestFactory extends SimpleControllerFactory {
    var controller: Class[_] = _
    override def controllerRegistered[C <: Controller](c: Class[C]) {
      controller = c
    }
  }
}