/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.config

import java.lang.annotation.Annotation
import scala.collection.JavaConversions._
import com.google.inject.{Injector, Scope, Scopes, Binding}
import com.google.inject.spi.BindingScopingVisitor
import com.coeusweb.Controller
import com.coeusweb.core.FrameworkException

/**
 * Register all the {@code Controller} classes from the bindings in a Guice
 * injector.
 */
private object GuiceRegistrar {
  
  /**
   * Register all the {@code Controller} classes from the specified Guice
   * {@code Injector}. 
   */
  def registerControllers(registry: ControllerRegistry, injector: Injector) {
    for {
      (key, binding) <- injector.getBindings
      controllerClass = key.getTypeLiteral.getRawType
      if classOf[Controller].isAssignableFrom(controllerClass)
    } {
      assertControllerScope(binding)
      registry.controllers += controllerClass.asInstanceOf[Class[Controller]]
    }
  }
  
  private def assertControllerScope(b: Binding[_]) {
    if (b.acceptScopingVisitor(hasScope))
      throw new FrameworkException("The Guice binding: " + b +
        " has wrong scope. Controller bindings must not have a scope (or have NO_SCOPE)" +
        " so that a new instance can be created on every request.")
  }
  
  private object hasScope extends BindingScopingVisitor[Boolean] {
    def visitNoScoping = false
    def visitEagerSingleton = true
    def visitScope(scope: Scope) = scope != Scopes.NO_SCOPE
    def visitScopeAnnotation(ann: Class[_ <: Annotation]) = true 
  }
}
