/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import javax.servlet.http._
import javax.servlet.{ServletConfig, ServletException}
import scala.collection.Map
import com.coeusweb.bind.ConverterRegistry
import com.coeusweb.i18n.locale.LocaleResolver
import com.coeusweb.{WebRequest, WebResponse}
import com.coeusweb.config._
import com.coeusweb.http.multipart.MultipartRequestParser
import com.coeusweb.interceptor.Interceptors
import com.coeusweb.scope.ApplicationScope

/**
 * DispatcherServlet is the entry point of the framework.
 */
class DispatcherServlet extends HttpServlet {
  
  /* Resolves a Handler for a given request */
  private[this] var resolver: RequestResolver = _
  
  /* Executes the request */
  private[this] var executor: RequestExecutor = _
  
  private[this] var localeResolver: LocaleResolver = _
  private[this] var converters: ConverterRegistry = _
  private[this] var requestEncoding: Option[String] = None
  private[this] var hideExistingResources: Boolean = _
  private[this] var multipartParser: MultipartRequestParser = _ 
  
  
  /*
   * Performs the servlet's initialization.
   */
  @throws(classOf[ServletException])
  override final def init(servletConfig: ServletConfig) {
    super.init(servletConfig)
    
    val (configBuilder, registrar) = ModuleLoader.loadModule(servletConfig)

    // setup configuration
    val dispatcherConfig = configBuilder.dispatcherConfig
    
    resolver = dispatcherConfig.requestResolver
    
    localeResolver = dispatcherConfig.localeResolver
    converters = dispatcherConfig.converters
    requestEncoding = dispatcherConfig.requestEncoding
    hideExistingResources = dispatcherConfig.hideExistingResources
    
    multipartParser = dispatcherConfig.multipartParser
    multipartParser.init(servletConfig.getServletContext)

    // register the controllers and interceptors
    val registry = new ControllerRegistry(dispatcherConfig)
    registrar.register(registry)

    // create the request executor
    executor = new RequestExecutor(new Interceptors(registry.interceptors),
                                   dispatcherConfig.exceptionHandler,
                                   dispatcherConfig.viewResolver,
                                   dispatcherConfig.controllerFactory)
    
    // setup the lock for ApplicationScope
    ApplicationScope.setupMutex(servletConfig.getServletContext)
  }
  
  override final def destroy() {
    multipartParser.destroy(getServletContext)
  }
  
  /*
   * Finds the Handler and executes the request.
   */
  override final protected def service(request: HttpServletRequest, response: HttpServletResponse) {
    
    // set the encoding, if configured
    for (encoding <- requestEncoding) {
      request.setCharacterEncoding(encoding)
      response.setCharacterEncoding(encoding)
    }
    
    // resolve and execute
    val path = removeContextFromPath(request.getRequestURI)
    resolver.resolve(path, Symbol(request.getMethod)) match {

      case HandlerNotFound =>
        response.sendError(HttpServletResponse.SC_NOT_FOUND)

      case MethodNotAllowed =>
        if (hideExistingResources) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND)
        } else {
          response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
        }

      case SuccessfulResolution(handler, pathVariables) =>
        if (WebRequest.isMultipart(request)) {
          val multipartRequest = multipartParser.parse(request) 
          execute(multipartRequest, response, handler, pathVariables)
          multipartParser.cleanupFiles(multipartRequest)
        } else {
          execute(request, response, handler, pathVariables)
        }
    }
  }
  
  private def execute(req: HttpServletRequest, res: HttpServletResponse, handler: Handler[_], vars: Map[String, String]) {
    executor.execute(new RequestContext(
      new WebRequest(req, vars, localeResolver, converters),
      new WebResponse(res),
      handler))
  }
  
  private def removeContextFromPath(requestUri: String) = {
    util.Strings.removePrefix(requestUri, getServletConfig.getServletContext.getContextPath)
  }
}
 

/**
 * Loads the configuration for the <code>DispatcherServlet</code>.
 */
private object ModuleLoader {
  
  /**
   * Load the configuration for the specified <code>ServletConfig</code>.
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
      case e: java.lang.reflect.InvocationTargetException => throw e.getCause
      case e: Exception => throw e
    }
  }
}
