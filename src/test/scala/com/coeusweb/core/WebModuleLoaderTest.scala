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
import config.WebModule

class WebModuleLoaderTest {
  
  import WebModuleLoaderTest._
  
  val servletConfig = new MockServletConfig("sweb-test")
  
  @Test(expected=classOf[javax.servlet.ServletException])
  def no_param_named_web_module_in_servlet_config() {
    loadModule()
  }
  
  @Test(expected=classOf[javax.servlet.ServletException])
  def module_class_not_found() {
    setModuleParam("not a class name")
    loadModule()
  }
  
  @Test(expected=classOf[javax.servlet.ServletException])
  def module_class_is_not_a_WebModule() {
    servletConfig.addInitParameter(paramName, "java.util.ArrayList")
    loadModule()
  }
  
  @Test(expected=classOf[IllegalStateException])
  def unwrap_any_exceptions_when_instantiating_the_module_class_via_reflection() {
    setModuleParam(classOf[ErroneousWebModule].getName)
    loadModule()
  }
  
  @Test 
  def successfully_load_the_web_module() {
    setModuleParam(classOf[EmptyWebModule].getName)
    val module = loadModule()
    assertEquals(false, module.hideResources)
    assertTrue(module.controllers.result.isEmpty)
    assertFalse(module.interceptors.result.isEmpty)
  }

  
  def paramName = WebModuleLoader.webModuleParamName

  def loadModule() = WebModuleLoader.load(servletConfig)
  
  def setModuleParam(moduleClass: String) {
    servletConfig.addInitParameter(paramName, moduleClass)
  }
}

object WebModuleLoaderTest {
  
  class EmptyWebModule(sc: ServletConfig) extends WebModule(sc)
  
  class ErroneousWebModule(sc: ServletConfig) extends WebModule(sc) {
    throw new IllegalStateException
  }
}
