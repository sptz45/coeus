/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import java.lang.annotation.Annotation
import java.lang.reflect.Method
import com.coeusweb.Controller
import com.coeusweb.annotation.Path
import com.coeusweb.core.convention._
import com.coeusweb.view.View

/**
 * Creates handler mappings from a controller class.
 */
private class HandlerMappingExtractor(
  classToPath: Class[_] => String,
  methodToPath: Method => String) {
  
  /**
   * Create hander mappings for the annotated methods of the specified controller
   * class.
   * 
   * @throws InvalidControllerClassException if the controller class has invalid
   *         structure or if it doesn't have handler annotations.
   */
  def extract[C <: Controller](controllerClass: Class[C]): Array[HandlerMapping] = {
    val mappings =
      for (method <- controllerClass.getMethods if isHandler(method))
      yield makeMapping(controllerClass, method)
    
    assertThat(!mappings.isEmpty, "No handler annotations were found in controller class: " + controllerClass)
    mappings
  }
  
  private def isHandler(m: Method) =
    m.getAnnotations.exists(AnnotationProcessor.toHttpMethod.isDefinedAt(_))
  
  private def makeMapping(c: Class[_], m: Method) = {
    assertMethodSignature(m)
    val a = getAnnotation(m)
    new HandlerMapping(makePath(c, m, a), getHttpMethod(a), m)
  }
  
  private def getAnnotation(m: Method) = {
    val annotations = m.getAnnotations.filter(AnnotationProcessor.toHttpMethod.isDefinedAt(_))
    assertThat(annotations.size == 1, "Found more that one HTTP annoations on method: ["+m+"]")
    annotations.head
  }

  private def getHttpMethod(a: Annotation): Symbol = AnnotationProcessor.toHttpMethod(a)
  
  private def makePath(c: Class[_], m: Method, a: Annotation) =
    makeAbsolutePath(mapClassName(c)) + makeAbsolutePath(mapMethodName(m, a))
    
  private def mapClassName(c: Class[_]): String = {
    for (ctrl <- c.getDeclaredAnnotations.collect{ case a: Path => a }) {
      return ctrl.value match {
        case "#" => classToPath(c)
        case path => path
      }
    }
    return classToPath(c)
  }
  
  private def mapMethodName(m: Method, a: Annotation): String = {
    val path = AnnotationProcessor.getValueFrom(a)
    if (path == "#") methodToPath(m) else path 
  }
  
  private def makeAbsolutePath(path: String) = path match {
    case "" => ""
    case _ if path(0) == '/' => path
    case _ => "/" + path
  }
  
  private def assertThat(assertion: Boolean, message: => String) {
    if (! assertion) throw new InvalidControllerClassException(message)
  }
  
  private def assertMethodSignature(m: Method) {
    def msg = "Method ["+m+"] has invalid signature for handler method. Handler methods must "
    val paramTypes = m.getParameterTypes
    assertThat(paramTypes.length == 0, msg + "take no arguments")
  }
}
