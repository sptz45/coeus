/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import java.lang.reflect.Method
import java.lang.reflect.InvocationTargetException
import com.coeusweb.controller.{ BeforeFilter, AfterFilter }
import com.coeusweb.{ WebRequest, WebResponse, Controller }
import com.coeusweb.view.View
import factory.ControllerFactory


/**
 * Handles web requests.
 * 
 * @param controllerClass the class of the Controller that will handle the request.
 * @param controllerMethod the Controller's method that will get invoked for handling the request
 * 
 * @see Controller
 * @see BeforeFilter
 * @see AfterFilter
 * @see {@link com.coeusweb.interceptor.Interceptor Interceptor}
 */
class Handler[T <: Controller](val controllerClass: Class[T], val controllerMethod: Method) {
  
  /**
   * Handle the given web request.
   * 
   * <p>The process this method follows for handling the request is:</p>
   * <ol>
   * <li>Create a new <code>Controller</code> instance using the specified <code>factory</code>.</li>
   * <li>Inject the specified <code>request</code> and <code>response</code> into the <code>Controller</code></li>
   * <li>If the <code>Controller</code> implements the <code>BeforeFilter</code> trait then calls the
   *     <code>before()</code> method</li>
   * <li>If the above method call returned a <code>View</code> then return that view else continue</code>
   * <li>Invoke the <code>Controller</code> method</li>
   * <li>If the <code>Controller</code> implements the <code>AfterFilter</code> trait then calls the
   *     <code>after()</code> method</li>
   * <li>If the {@code after()} method returned some {@code View} then return that view else return the
   *     result of the invocation of the <code>Controller</code> method</li>
   * </ol>
   * 
   * <p>If the <code>Controller</code> implements the <code>AfterFilter</code> interface and an
   * uncaught exception occurs then the <code>AfterFilter.after()</code> method will
   * get called with the uncaught exception. If the <code>after()</code> method does not
   * throw an exception or return some view the uncaught exception will get re-thrown by the
   * framework.</p>
   * 
   * @param factory the factory that will be used for instantiating a new controller
   * @param request the current request
   * @param response the current response
   * 
   * @return if the <code>Controller</code> implements the <code>BeforeFilter</code> trait and
   * the method {@link BeforeFilter#before()} returns a <code>View</code> then that
   * view gets returned, else if the <code>Controller</code> implements the {@code AfterFilter} trait and
   * the method {@link AfterFilter#after()} returns a {@code View} then that view gets returned, else  
   * returns the result of invoking the {@code Controller}'s handler method.
   */
  def handle(factory: ControllerFactory, request: WebRequest, response: WebResponse): Any = {
    
    val controller = factory.createController(controllerClass)
    controller.request = request
    controller.response = response
    
    var throwable: Throwable = null
    
    var result: Any = controller match {
      case filter: BeforeFilter => filter.before().getOrElse(null)
      case _ => null
    }
    
    if (result == null) {
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
}
