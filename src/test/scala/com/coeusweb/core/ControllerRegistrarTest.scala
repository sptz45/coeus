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
import com.coeusweb.mvc.controller.Controller
import com.coeusweb.mvc.annotation.Get
import config.DispatcherConfig

class ControllerRegistrarTest {
  
  import ControllerRegistrarTest._

  private val config = new CustomConfig
  private val registrar = new ControllerRegistrar(config)
  
  @Test
  def register_a_controller() {
    register(new ProjectController)
    assertHandlerFound("/project/list")
  }
  
  def register(c: Controller) {
    registrar.registerAll(List(c))
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
  
  class ProjectController extends Controller {
    @Get def list() = "projects"
  }
  
  class CustomConfig(
    val servletConfig: ServletConfig = new MockServletConfig("test-servlet"))
      extends DispatcherConfig
}
