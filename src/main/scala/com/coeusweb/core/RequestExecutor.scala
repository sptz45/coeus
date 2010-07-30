/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import scala.xml.NodeSeq
import com.coeusweb.error.{ ExceptionHandler, ErrorPageView }
import com.coeusweb.interceptor.Interceptor
import com.coeusweb.view._
import convention.RequestToViewNameTranslator.viewNameForRequest
import factory.ControllerFactory


private class RequestExecutor(
  interceptors: Iterable[Interceptor],
  exceptionHandler: ExceptionHandler,
  viewResolver: ViewResolver,
  controllerFactory: ControllerFactory) {
  
  def execute(context: RequestContext) {
    import context._
    
    val pipeline = new InterceptorPipeline(interceptors, exceptionHandler)
    
    val callHandler = pipeline.executePreHandle(context)
    
    if (callHandler)
      executeHandler(context)
    
    pipeline.executePostHandle(context)
    
    renderView(context)
    
    pipeline.executeAfterRender(context)
    
    if (shouldPropagateAnyException(context))
      throw error
  }
  
  
  private def executeHandler(context: RequestContext) {
    import context._
    
    try {
      val result = handler.handle(controllerFactory, request, response)
      context.result = result
    
    } catch {
      case t =>
        context.error = t
        context.result = exceptionHandler.handle(context)
    }
  }
  
  private def renderView(context: RequestContext) {
    import context._
    
    def processViewName(name: String): View = {
      val view = viewResolver.resolve(name)
      if (view eq null) {
        context.error = new NoViewFoundException(name)
        ErrorPageView
      } else {
        view.render(request, response)
        view
      }
    }

    context.result = result match {
      
      case null =>
        processViewName(viewNameForRequest(request))

      case name: String =>
        processViewName(name)

      case xml: NodeSeq =>
        val view = new XhtmlView(xml)
        view.render(request, response)
        view
        
      case ViewReference(name) =>
        processViewName(name)

      case view: View =>
        view.render(request, response)
        view
    }
  }
  
  private def shouldPropagateAnyException(context: RequestContext) = context.result == ErrorPageView
}
