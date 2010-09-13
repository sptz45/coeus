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
import org.springframework.mock.web.MockServletConfig
import com.coeusweb.mvc.annotation.Get
import com.coeusweb.mvc.controller.Controller
import com.coeusweb.mvc.scope.ApplicationScope
import config.DispatcherConfig

class ControllerRegistrarTest {
  
  import ControllerRegistrarTest._

  val config = new CustomConfig
  val application = new ApplicationScope(null)
  private val registrar = new ControllerRegistrar(config, application)
  
  @Test
  def register_a_controller() {
    register(new ProjectController)
    assertHandlerFound("/project/list")
  }
  
  @Test
  def a_registered_controller_gets_injected() {
    val controller = new ProjectController 
    register(controller)
    assertEquals(application, controller.application)
    assertEquals(config.messageBundle, controller.messageBundle)
    assertEquals(config.converters, controller.converters)
  }
  
  def register(c: Controller) {
    registrar.registerAll(List(c))
  }
  
  def assertHandlerFound(path: String, method: Symbol = 'GET) {
    val (handlers, _) = config.requestResolver.resolve(path)
    assert(!handlers.isEmpty,
           "No handler found for path %s".format(path))
    assert(handlers.isMethodAllowed(method),
           "Method %s not allowed for path %s".format(method.toString, path))
  }
}

object ControllerRegistrarTest {
  
  class ProjectController extends Controller {
    @Get def list() = "projects"
  }
  
  class CustomConfig(
    val servletConfig: ServletConfig = new MockServletConfig("test-servlet"))
      extends DispatcherConfig
}
