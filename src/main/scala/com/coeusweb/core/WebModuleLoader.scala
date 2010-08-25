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
private object WebModuleLoader {
  
  def load(config: ServletConfig): WebModule = {

    val moduleClassName = config.getInitParameter("web-module")
    if (moduleClassName eq null)
      throw new ServletException(
        "Could not initialize DispatcherServlet '%s' because no servlet init parameter named 'context' was configured in web.xml for the servlet"
        .format(config.getServletName))

    val moduleClass = {
      try {
        this.getClass.getClassLoader.loadClass(moduleClassName)
      } catch {
        case e: ClassNotFoundException =>
          throw new ServletException(
            "Could not initialize DispatcherServlet %s because the module class [%s] was not found."
              .format(config.getServletName, moduleClassName), e)
      }
    }

    if (! classOf[WebModule].isAssignableFrom(moduleClass)) {
      throw new ServletException(
        "Could not initialize DispatcherServlet %s because the module class [%s] does not extend %s"
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
