/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.factory

import java.lang.reflect.Method
import com.coeusweb.Controller
import com.coeusweb.FrameworkException
import com.coeusweb.core.convention.Conventions

/**
 * Creates {@code Controller} instances by invoking methods on a "factory" object.
 * 
 * <p>For each registered {@code Controller} class the "factory" object must have
 * a method that takes no arguments, returns an instance of that class and its
 * name can be derived using the {@link #classToMethodName(Class)} method.</p>
 * 
 * <p>The methods of the "factory" object must always return a new instance of a
 * {@code Controller}.</p> 
 * 
 * @param factory an object with methods for creating controllers
 * @see Controller
 */
class MethodInvokingControllerFactory(factory: AnyRef) extends ControllerFactory {

  private[this] var cache = Map[Class[_], Method]()

  def controllerRegistered[C <: Controller](controllerClass: Class[C]) {
    try {
      val factoryMethod = factory.getClass.getMethod(classToMethodName(controllerClass)) 
      require(factoryMethod.getReturnType == controllerClass)
      cache = cache + (controllerClass -> factoryMethod)
    } catch {
      case e: Exception => throw new FrameworkException(
        "To register the controller class: %s, the class: %s must have a method named: %s with no arguments and return type: %s"
          .format(controllerClass.getName,
                  factory.getClass.getName,
                  classToMethodName(controllerClass),
                  controllerClass.getName), e)
    }
  }
  
  def createController[C <: Controller](klass: Class[C]): C = {
    try {
      cache(klass).invoke(factory).asInstanceOf[C]
    } catch {
      case e: NoSuchElementException =>
        throw new FrameworkException(
          "Could not instantiate a controller of class: %s because the class hasn't been registered"
            .format(klass.getName))
    }
  }
  
  /**
   * Derive a method name for the specified controller class.
   * 
   * @param controllerClass the class of the controller
   * 
   * @return the simple name of the controller class with the first character
   *         converted to lower case
   */
  protected def classToMethodName(controllerClass: Class[_]) = Conventions.classToAttributeName(controllerClass)
}
