/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.factory

import java.lang.reflect.Method
import com.coeusweb.Controller
import com.coeusweb.config.DispatcherConfig
import com.coeusweb.core.FrameworkException
import com.coeusweb.core.util.Strings.firstCharToLower

class MethodInvokingControllerFactory(val delegate: AnyRef) extends ControllerFactory {

  private[this] var cache = Map[Class[_], Method]()

  def registerClass[C <: Controller](controllerClass: Class[C]) {
    try {
      val factoryMethod = delegate.getClass.getMethod(classToMethodName(controllerClass)) 
      require(factoryMethod.getReturnType == controllerClass)
      cache = cache + (controllerClass -> factoryMethod)
    
    } catch {
      case e: Exception => throw new FrameworkException(
        "To register the controller class: %s, the class: %s must have a method named: %s with no arguments and return type: %s"
          .format(controllerClass.getName,
            delegate.getClass.getName,
            classToMethodName(controllerClass),
            controllerClass.getName), e)
    }
  }
  
  def createController[C <: Controller](klass: Class[C]): C = {
    try {
      cache(klass).invoke(delegate).asInstanceOf[C]
    } catch {
      case e: NoSuchElementException =>
        throw new FrameworkException(
          "Could not instantiate a controller of class: %s because the class hasn't been registered"
            .format(klass.getName))
    }
  }
  
  protected def classToMethodName(c: Class[_]) = firstCharToLower(c.getSimpleName)
}