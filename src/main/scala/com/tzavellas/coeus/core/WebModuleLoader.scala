/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core

import java.lang.reflect.InvocationTargetException 
import javax.servlet._
import config.WebModule

/**
 * Loads the configuration for the {@code DispatcherServlet}.
 */
private object WebModuleLoader {
  
  def webModuleParamName = "web-module"
  
  def load(config: ServletConfig): WebModule = {

    val moduleClassName = config.getInitParameter(webModuleParamName)
    if (moduleClassName eq null)
      throw new ServletException(
        "Could not initialize DispatcherServlet %s because no servlet init parameter named %s " +
        "was configured in web.xml for the servlet."
          .format(config.getServletName, webModuleParamName))

    val moduleClass = {
      try {
        this.getClass.getClassLoader.loadClass(moduleClassName)
      } catch {
        case e: ClassNotFoundException =>
          throw new ServletException(
            "Could not initialize DispatcherServlet %s because the web-module class [%s] was not found."
              .format(config.getServletName, moduleClassName), e)
      }
    }

    if (! classOf[WebModule].isAssignableFrom(moduleClass)) {
      throw new ServletException(
        "Could not initialize DispatcherServlet %s because the web-module class [%s] does not extend %s"
          .format(config.getServletName, moduleClassName, classOf[WebModule].getName))
    }

    try {
      val module = moduleClass.getConstructor(classOf[ServletConfig]).newInstance(config)
      module.asInstanceOf[WebModule]
    } catch {
      case e: InvocationTargetException => throw e.getCause
    }
  }
}
