/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import java.lang.reflect.{ Method, InvocationTargetException }
import com.coeusweb.mvc.{ WebRequest, WebResponse }
import com.coeusweb.mvc.controller.{ BeforeFilter, AfterFilter, Controller }

/**
 * Handles web requests.
 * 
 * @param controller       the Controller instance that will handle the request.
 * @param controllerMethod the Controller's method that will get invoked for
 *                         handling the request.
 * 
 * @see Controller
 * @see BeforeFilter
 * @see AfterFilter
 */
class Handler(val controller: Controller, val controllerMethod: Method) {
  
  /**
   * Handle the given web request.
   *
   * <p>The process this method follows for handling the request is:</p>
   * <ol>
   * <li>If the {@code Controller} implements the {@code BeforeFilter} trait
   *     then call the {@code before()} method.</li>
   * <li>If the above method call returns {@code None} then invoke the
   *     {@code Controller}'s handler method ({@code controllerMethod}).</li>
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
   *         trait and the method {@link BeforeFilter#before()} returns a
   *         {@code View} then that view gets returned, else if the
   *         {@code Controller} implements the {@code AfterFilter} trait and
   *         the method {@link AfterFilter#after()} returns a {@code View} then
   *         that view gets returned, else returns the result of invoking the
   *         {@code Controller}'s handler method.
   */
  def handle(request: WebRequest, response: WebResponse): Any = {
    
    var throwable: Throwable = null
    
    var result: AnyRef = controller match {
      case filter: BeforeFilter => filter.before().getOrElse(null)
      case _ => null
    }
    
    if (result eq null) {
      try {
        result = controllerMethod.invoke(controller)
      } catch {
        case e: InvocationTargetException => throwable = e.getCause
      }
    }
    
    val afterResult = controller match {
      case filter: AfterFilter => filter.after(Option(throwable))
      case _ => None
    }
    
    if (afterResult.isDefined) return afterResult.get
    
    if (throwable != null) throw throwable
    result
  }

  //override def toString = controller.getClass.getName+"#"+controllerMethod.getName
}
