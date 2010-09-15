/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import javax.servlet.http._
import javax.servlet.{ ServletConfig, ServletException }
import scala.collection.Map
import com.coeusweb.bind.ConverterRegistry
import com.coeusweb.i18n.locale.LocaleResolver
import com.coeusweb.i18n.msg.MessageBundle
import com.coeusweb.http.MutableHttpServletRequest
import com.coeusweb.http.multipart.MultipartRequestParser
import com.coeusweb.mvc.{ WebRequest, WebResponse }
import com.coeusweb.mvc.scope.ApplicationScope
import com.coeusweb.util.internal.Strings

import config.WebModule

/**
 * DispatcherServlet is the entry point of the framework.
 */
class DispatcherServlet extends HttpServlet {
  
  /* Resolves a Handler for a given request */
  private[this] var resolver: RequestResolver = _
  
  /* Executes the request */
  private[this] var executor: RequestExecutor = _
  
  private[this] var localeResolver: LocaleResolver = _
  private[this] var messageBundle: MessageBundle = _
  private[this] var converters: ConverterRegistry = _
  private[this] var requestEncoding: String = _
  private[this] var hideResources: Boolean = _
  private[this] var overrideHttpMethod: Boolean = _
  private[this] var allowHttpHead: Boolean = _
  private[this] var allowHttpOptions: Boolean = _
  private[this] var multipartParser: MultipartRequestParser = _

  private[this] var applicationScope: ApplicationScope = _
  
  
  /*
   * Performs the servlet's initialization.
   */
  @throws(classOf[ServletException])
  override final def init(servletConfig: ServletConfig) {
    super.init(servletConfig)
    
    val module = WebModuleLoader.load(servletConfig)

    resolver           = module.requestResolver    
    localeResolver     = module.localeResolver
    messageBundle      = module.messageBundle
    converters         = module.converters
    requestEncoding    = module.requestEncoding
    hideResources      = module.hideResources
    overrideHttpMethod = module.overrideHttpMethod
    allowHttpHead      = module.allowHttpHead
    allowHttpOptions   = module.allowHttpOptions
    
    multipartParser = module.multipartParser
    multipartParser.init(servletConfig.getServletContext)
    
    // setup ApplicationScope
    applicationScope = new ApplicationScope(servletConfig.getServletContext)
    ApplicationScope.setupMutex(servletConfig.getServletContext)

    // register the configured controller classes
    val registrar = new ControllerRegistrar(module, applicationScope)
    registrar.registerAll(module.controllers.result)

    // create the request executor
    executor = new RequestExecutor(module.interceptors.result,
                                   module.exceptionHandler,
                                   module.viewResolver)
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
    val method = getHttpMethod(request)

    val (handlers, pathVariables) = resolver.resolve(path)
    
    if (handlers.isEmpty) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND)
      return
    }
    
    if (! handlers.isMethodAllowed(method)) {
      if (method == 'OPTIONS && allowHttpOptions) {
        HttpOptionsResponseGenerator.generate(response, handlers, allowHttpHead)
        return
      }

      if (method == 'HEAD && allowHttpHead && handlers.isMethodAllowed('GET)) {
        execute(request, response, handlers('GET), pathVariables)
        return
      }

      if (hideResources) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND)
      } else {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
      }
      return
    }
    
    val handler = handlers(method)
    if (WebRequest.isMultipart(request)) {
      val multipartRequest = multipartParser.parse(request)
      if (mustChangeMethod(request))
        multipartRequest.setMethod(method.name)
        execute(multipartRequest, response, handler, pathVariables)
        multipartParser.cleanupFiles(multipartRequest)
    } else {
      var wrappedReq = request 
      if (mustChangeMethod(request)) {
        wrappedReq = new MutableHttpServletRequest(request, method.name)
      }
      execute(wrappedReq, response, handler, pathVariables)
    }
  }
  
  private def execute(req: HttpServletRequest,
                      res: HttpServletResponse,
                      handler: Handler,
                      vars: Map[String, String]) {
    
    executor.execute(new RequestContext(
      new WebRequest(applicationScope, req, vars, localeResolver, converters, messageBundle),
      new WebResponse(res),
      handler))
  }
  
  private def removeContextFromPath(requestUri: String) = {
    Strings.removePrefix(requestUri, getServletConfig.getServletContext.getContextPath)
  }
  
  private def mustChangeMethod(req: HttpServletRequest): Boolean = {
    if (!overrideHttpMethod) return false
    val _method = req.getParameter("_method")
    if (_method ne null) req.getMethod != _method.toUpperCase else false
  }
  
  private def getHttpMethod(req: HttpServletRequest): Symbol = {
    if (!overrideHttpMethod) return Symbol(req.getMethod)
    val _method = req.getParameter("_method")
    Symbol(if (_method eq null) req.getMethod else _method.toUpperCase)
  }
}
