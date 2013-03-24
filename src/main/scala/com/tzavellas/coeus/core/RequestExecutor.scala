/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core

import scala.xml.NodeSeq
import com.tzavellas.coeus.mvc.view._
import com.tzavellas.coeus.mvc.util.Conventions.viewNameForRequest
import error.{ ExceptionHandler, ErrorPageView }
import interception.Interceptor


private class RequestExecutor(
  interceptors: Iterable[Interceptor],
  exceptionHandler: ExceptionHandler,
  viewResolver: ViewResolver) {
  
  def execute(context: RequestContext) {
    import context._
    
    val pipeline = new InterceptorPipeline(interceptors, exceptionHandler)
    
    val callHandler = pipeline.executePreHandle(context)
    
    if (callHandler)
      executeHandler(context)
    
    pipeline.executePostHandle(context)
    
    renderView(context)
    
    pipeline.executeAfterRender(context)
    
    if (shouldPropagateException(context))
      throw error
  }
  
  
  private def executeHandler(context: RequestContext) {
    try {
      context.result = context.handler.handle()
    } catch {
      case e: Exception =>
        context.error = e
        context.result = exceptionHandler.handle(context)
    }
  }
  
  private def renderView(context: RequestContext) {
    import context._
    
    def findByName(name: String): View = {
      val view = viewResolver.resolve(name)
      if (view eq null)
        throw new NoViewFoundException(name)
      view
    }
    
    def findView(): View = result match {
      case null           => findByName(viewNameForRequest(request))
      case name: String   => findByName(name)
      case ViewName(name) => findByName(name)
      case view: View     => view
      case xml: NodeSeq   => new XhtmlView(xml)
      case unit: Unit     => findByName(viewNameForRequest(request))
      case _              =>
        throw new InvalidControllerClassException(
          "Controller method ["+handler.method+"] does not have a valid return type. " +
          "Valid return types are: String, NodeSeq, View and Unit.")
    }
    
    try {
      findView().render(request, response)
    } catch {
      case e: Exception =>
        error = e
        val errorView = exceptionHandler.handle(context)
        result = errorView
        if (! response.isCommited)
          errorView.render(request, response)
    }
  }
  
  private def shouldPropagateException(context: RequestContext) = context.result == ErrorPageView
}
