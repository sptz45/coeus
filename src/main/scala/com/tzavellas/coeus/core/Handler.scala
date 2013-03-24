/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core

import java.lang.reflect.{ Method, InvocationTargetException }
import com.tzavellas.coeus.mvc.{ WebRequest, WebResponse }
import com.tzavellas.coeus.mvc.controller.{ BeforeFilter, AfterFilter, Controller }

/**
 * Handles web requests.
 * 
 * @param controller the Controller instance that will handle the request.
 * @param method     the Controller's method that will get invoked for
 *                   handling the request.
 * 
 * @see Controller
 * @see BeforeFilter
 * @see AfterFilter
 */
class Handler(val controller: Controller, val method: Method) {
  
  /**
   * Handle the current web request.
   *
   * <p>The process this method follows for handling the request is:</p>
   * <ol>
   * <li>If the {@code Controller} implements the {@code BeforeFilter} trait
   *     then call the {@code before()} method.</li>
   * <li>If the above method call does not throw a {@code BeforeFilter.Throwable}
   *     then invoke the {@code Controller}'s handler method.</li>
   * <li>If the {@code Controller} implements the {@code AfterFilter} trait
   *     then call the {@code after()} method.</li>
   * </ol>
   *
   * <p>If the {@code Controller} implements the {@code AfterFilter} trait and
   * an uncaught exception occurs during the execution of the controller's
   * handler method then the {@code AfterFilter.after()} method will get called
   * with the uncaught exception. If the {@code after()} method does not throw
   * an exception or return some view the uncaught exception will get re-thrown.
   * </p>
   *
   * @param request  the current request
   * @param response the current response
   *
   * @return if the {@code Controller} implements the {@code BeforeFilter}
   *         trait and the method {@link BeforeFilter#before()} throws a
   *         {@code BeforeFilter.Throwable} then the view of the interruption
   *         gets returned, else if the {@code Controller} implements the
   *         {@code AfterFilter} trait and the method
   *         {@link AfterFilter#after()} returns a {@code View} then that view
   *         gets returned, else returns the result of invoking the
   *         {@code Controller}'s handler method.
   */
  def handle(): Any = {
    
    var result: Any = null
    var exception: Exception = null
    
    try {

      controller match {
        case filter: BeforeFilter => filter.before()
        case _                    => ()
      }

      result = method.invoke(controller)
      
    } catch {
      case t: BeforeFilter.Throwable =>
        result = t.view
      case e: InvocationTargetException => e.getCause match {
        case exc: Exception => exception = exc
        case throwable      => throw throwable
      }
    }
    
    val afterResult = controller match {
      case filter: AfterFilter => filter.after(Option(exception))
      case _                   => None
    }

    if (afterResult.isDefined) return afterResult.get

    if (exception != null) throw exception
    result
  }

  //override def toString = controller.getClass.getName+"#"+method.getName
}
