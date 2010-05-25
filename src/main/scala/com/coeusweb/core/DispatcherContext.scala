/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import java.lang.reflect.InvocationTargetException 
import javax.servlet._
import com.coeusweb.config._

/**
 * Loads the configuration for the {@code DispatcherServlet}.
 */
private class DispatcherContext(config: ServletConfig) {
  
  private var dispatcherContext: ConfigBuilder with ControllerRegistry = _
  
  val moduleClassName = config.getInitParameter("context")
  if (moduleClassName eq null)
    throw new ServletException(
        "Could not initialize DispatcherServlet '%s' because no servlet init parameter named 'context' was configured in web.xml for the servlet"
        .format(config.getServletName))

  val contextClass = {
    try {
      this.getClass.getClassLoader.loadClass(moduleClassName)
    } catch {
      case e: ClassNotFoundException =>
        throw new ServletException(
          "Could not initialize DispatcherServlet %s because the module class [%s] was not found."
            .format(config.getServletName, moduleClassName), e)
    }
  }

  if (! classOf[ConfigBuilder].isAssignableFrom(contextClass)) {
    throw new ServletException(
      "Could not initialize DispatcherServlet %s because the module class [%s] does not extend %s"
      .format(config.getServletName, moduleClassName, classOf[DispatcherConfig].getName))
  }

  if (! classOf[ControllerRegistry].isAssignableFrom(contextClass)) {
    throw new ServletException(
      "Could not initialize DispatcherServlet %s because the module class [%s] does not extend %s"
        .format(config.getServletName, moduleClassName, classOf[ControllerRegistry].getName))
  }

  try {
    val context = contextClass.getConstructor(classOf[ServletConfig]).newInstance(config)
    dispatcherContext = context.asInstanceOf[ConfigBuilder with ControllerRegistry]
  } catch {
    case e: InvocationTargetException => throw e.getCause
    case e: Exception => throw e
  }
  
  def dispatcherConfig = dispatcherContext.dispatcherConfig
  
  def controllers = dispatcherContext.controllers
  
  def interceptors = dispatcherContext match {
    case ir: InterceptorRegistry => ir.interceptors
    case _                       => (new InterceptorRegistry { }).interceptors
  }
}
