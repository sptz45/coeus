/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core.error

import com.tzavellas.coeus.mvc.view.View
import com.tzavellas.coeus.core.RequestContext

/**
 * A handler that gets called when an uncaught exception occurs during
 * request execution.
 * 
 * <p>Instances of this class must be thread-safe.</p>
 */
trait ExceptionHandler {

  /**
   * Handle the error that occurred during the execution of the specified
   * request.
   * 
   * <p>Note that the returned <code>View</code> might not get used if
   * a response is already sent to the client.</p>
   * 
   * @param context the context of the current request
   * @return a view that will be used to render the response
   */
  def handle(context: RequestContext): View
}


/**
 * Contains factory methods for creating {@code ExceptionHandler} instances.
 */
object ExceptionHandler {
  
  /**
   * Returns a handler that can be used to propage all the uncaught exceptions
   * to the servlet container.
   * 
   * <p>If the exception that occurred is an instance of {@code HttpException}
   * then the returned handler also sets the HTTP status code of the response
   * using the {@link HttpException#httpStatus} field of the exception.</p>
   * 
   * <p>The returned handler will always return {@link ErrorPageView}.<p>
   */
  def defaultHandler(servletName: String): ExceptionHandler = {
    new ExceptionHandler {
      def handle(context: RequestContext) = {
        ErrorUtils.setupErrorPageAttributes(context.request, context.error, servletName)
        ErrorUtils.setResposeStatus(context.response, context.error)
        ErrorPageView
      }
    }
  }
  
  /**
   * Create a handler that will use the specified partial function to find a view
   * for the uncaught exception.
   * 
   * <p>If a view cannot be found then the handler will return {@link ErrorPageView}.</p>
   * 
   * <p>If the exception that occurred is an instance of {@code HttpException}
   * then the returned handler also sets the HTTP status code of the response
   * using the {@link com.tzavellas.coeus.HttpException#httpStatus httpStatus} field
   * of the exception.</p>
   */
  def forServlet(servletName: String)(errorViewMap: PartialFunction[Throwable, View]): ExceptionHandler = {
    val views: Throwable => View = errorViewMap orElse { case _ => ErrorPageView }
    new ExceptionHandler {
      def handle(context: RequestContext) = {
        ErrorUtils.setupErrorPageAttributes(context.request, context.error, servletName)
        ErrorUtils.setResposeStatus(context.response, context.error)
        views(context.error)
      }
    }
  }
}