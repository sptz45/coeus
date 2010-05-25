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
import com.coeusweb.config._

class DispatcherServletConfigTest {
  import DispatcherServletConfigTest._
  
  val servlet = new DispatcherServlet
  val servletConfig = new MockServletConfig("sweb-test")
  
  @Test(expected=classOf[javax.servlet.ServletException])
  def no_context_configured() {
    servlet.init(servletConfig)
  }
  
  @Test(expected=classOf[javax.servlet.ServletException])
  def context_class_not_found() {
    servletConfig.addInitParameter("context", "not a class name")
    servlet.init(servletConfig)
  }
  
  @Test(expected=classOf[javax.servlet.ServletException])
  def context_class_is_not_a_module_config_builder() {
    servletConfig.addInitParameter("context", "java.util.ArrayList")
    servlet.init(servletConfig)
  }
  
  @Test(expected=classOf[javax.servlet.ServletException])
  def context_class_is_not_a_controller_registry() {
    servletConfig.addInitParameter("context", classOf[EmptyContextConfigBuilder].getName)
    servlet.init(servletConfig)
  }
  
  @Test 
  def successfully_load_the_context_configuration() {
    servletConfig.addInitParameter("context", classOf[EmptyContext].getName)
    servlet.init(servletConfig)
  }
  
  @Test(expected=classOf[RuntimeException])
  def unwrap_any_exceptions_when_instantiating_the_context_via_reflection() {
    servletConfig.addInitParameter("context", classOf[ErroneousContext].getName)
    servlet.init(servletConfig)
  }
}


object DispatcherServletConfigTest {

  class EmptyContextConfigBuilder(sc: ServletConfig) extends ConfigBuilder(sc)
  
  class EmptyContext(sc: ServletConfig) extends EmptyContextConfigBuilder(sc) with ControllerRegistry
  
  class ErroneousContext(sc: ServletConfig) extends EmptyContextConfigBuilder(sc) with ControllerRegistry {
    throw new RuntimeException
  }
}