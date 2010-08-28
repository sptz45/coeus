/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import javax.servlet.ServletConfig
import org.junit.Test
import org.junit.Assert._
import com.coeusweb.test.servlet.MockServletConfig
import com.coeusweb.Controller
import com.coeusweb.annotation.Get
import com.coeusweb.config.DispatcherConfig
import com.coeusweb.core.factory._

class ControllerRegistrarTest {
  import ControllerRegistrarTest._

  val factory = new TestFactory
  val config = new CustomConfig(factory)
  private val registrar = new ControllerRegistrar(config)
  
  @Test
  def register_a_controller() {
    register[ProjectController]
    assertHandlerFound("/project/list")
  }
  
  @Test
  def a_controller_class_does_not_get_registered_if_it_is_abstract() {
    register[AbstractController]
    assertNoHanderFound("/abstract/index")
  }
  
  @Test
  def when_a_controller_gets_registered_a_callback_is_made_into_the_controller_facory() {
    register[ProjectController]
    assertEquals(classOf[ProjectController], factory.controller)
  }
  
  def register[C <: Controller](implicit cm: ClassManifest[C]) {
    registrar.registerAll(List(cm.erasure.asInstanceOf[Class[Controller]]))
  }
  
  def assertHandlerFound(path: String, m: Symbol = 'GET) {
    config.requestResolver.resolve(path, m) match {
      case HandlerNotFound  => fail("No handler found for path %s".format(path))
      case MethodNotAllowed => fail("Method %s not allowed for path %s".format(m.toString, path))
      case _                => ()
    }
  }

  def assertNoHanderFound(path: String) {
    assertEquals(HandlerNotFound, config.requestResolver.resolve(path, 'GET)) 
  }
}

object ControllerRegistrarTest {
  
  abstract class AbstractController extends Controller {
    @Get def index() = "index"
  }
  
  class ProjectController extends Controller {
    @Get def list() = "projects"
  }
  
  class CustomConfig(
    factory: ControllerFactory,
    val servletConfig: ServletConfig = new MockServletConfig("test-servlet"))
      extends DispatcherConfig {
    controllerFactory = factory 
  }
  
  class TestFactory extends SimpleControllerFactory {
    var controller: Class[_] = _
    override def controllerRegistered[C <: Controller](c: Class[C]) {
      controller = c
    }
  }
}
