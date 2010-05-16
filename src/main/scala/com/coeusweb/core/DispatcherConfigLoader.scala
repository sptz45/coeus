package com.coeusweb.core

import java.lang.reflect.InvocationTargetException 
import javax.servlet._
import com.coeusweb.config._

/**
 * Loads the configuration for the {@code DispatcherServlet}.
 */
private object DispatcherConfigLoader {
  
  /**
   * Load the configuration for the specified {@code ServletConfig}.
   * 
   * @throws ServletException if the configuration is not valid.
   */
  def loadModule(config: ServletConfig): (ConfigBuilder, ControllerRegistrar) = {
    val moduleClassName = config.getInitParameter("module")
    if (moduleClassName eq null)
      throw new ServletException(
        "Could not initialize DispatcherServlet '%s' because no module is configured in web.xml for the servlet"
          .format(config.getServletName))
    
    val moduleClass =
      try {
        this.getClass.getClassLoader.loadClass(moduleClassName)
      } catch {
        case e: ClassNotFoundException =>
          throw new ServletException(
            "Could not initialize DispatcherServlet %s because the module class [%s] was not found."
              .format(config.getServletName, moduleClassName), e)
      }
    
    if (! classOf[ConfigBuilder].isAssignableFrom(moduleClass)) {
      throw new ServletException(
        "Could not initialize DispatcherServlet %s because the module class [%s] does not extend %s"
          .format(config.getServletName, moduleClassName, classOf[DispatcherConfig].getName))
    }
    
    if (! classOf[ControllerRegistrar].isAssignableFrom(moduleClass)) {
      throw new ServletException(
        "Could not initialize DispatcherServlet %s because the module class [%s] does not extend %s"
          .format(config.getServletName, moduleClassName, classOf[ControllerRegistrar].getName))
    }
    
    try {
      val module = moduleClass.getConstructor(classOf[ServletConfig]).newInstance(config)
      (module.asInstanceOf[ConfigBuilder] -> module.asInstanceOf[ControllerRegistrar])
    } catch {
      case e: InvocationTargetException => throw e.getCause
      case e: Exception => throw e
    }
  }
}