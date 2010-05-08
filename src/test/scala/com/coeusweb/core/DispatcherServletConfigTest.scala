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
  def no_module_configured() {
    servlet.init(servletConfig)
  }
  
  @Test(expected=classOf[javax.servlet.ServletException])
  def module_class_not_found() {
    servletConfig.addInitParameter("module", "not a class name")
    servlet.init(servletConfig)
  }
  
  @Test(expected=classOf[javax.servlet.ServletException])
  def module_class_is_not_a_module_config_builder() {
    servletConfig.addInitParameter("module", "java.util.ArrayList")
    servlet.init(servletConfig)
  }
  
  @Test(expected=classOf[javax.servlet.ServletException])
  def module_class_is_not_a_controller_registrar() {
    servletConfig.addInitParameter("module", classOf[EmptyModuleConfigBuilder].getName)
    servlet.init(servletConfig)
  }
  
  @Test 
  def successfully_load_the_module_configuration() {
    servletConfig.addInitParameter("module", classOf[EmptyModule].getName)
    servlet.init(servletConfig)
  }
}


object DispatcherServletConfigTest {
  
  class EmptyModuleConfigBuilder(sc: ServletConfig) extends ConfigBuilder(sc)
  
  class EmptyModule(sc: ServletConfig) extends EmptyModuleConfigBuilder(sc) with ControllerRegistrar {
    def register(r: ControllerRegistry) { }
  }
}