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
  private[this] var requestEncoding: String = _
  private[this] var hideResources: Boolean = _
  private[this] var multipartParser: MultipartRequestParser = _ 
  
  
  /*
   * Performs the servlet's initialization.
   */
  @throws(classOf[ServletException])
  override final def init(servletConfig: ServletConfig) {
    super.init(servletConfig)
    
    val webModule = WebModuleLoader.load(servletConfig)

    // setup configuration
    val dispatcherConfig = webModule
    
    resolver = dispatcherConfig.requestResolver
    
    localeResolver = dispatcherConfig.localeResolver
    converters = dispatcherConfig.converters
    requestEncoding = dispatcherConfig.requestEncoding
    hideResources = dispatcherConfig.hideResources
    
    multipartParser = dispatcherConfig.multipartParser
    multipartParser.init(servletConfig.getServletContext)

    // register the configured controller classes
    new ControllerRegistrar(dispatcherConfig).registerAll(webModule.controllers.result)

    // create the request executor
    executor = new RequestExecutor(webModule.interceptors.result,
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
    if (requestEncoding ne null) {
      request.setCharacterEncoding(requestEncoding)
      response.setCharacterEncoding(requestEncoding)
    }
    
    // resolve and execute
    val path = removeContextFromPath(request.getRequestURI)
    resolver.resolve(path, Symbol(request.getMethod)) match {

      case HandlerNotFound =>
        response.sendError(HttpServletResponse.SC_NOT_FOUND)

      case MethodNotAllowed =>
        if (hideResources) {
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
