/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.config

import java.lang.annotation.Annotation
import scala.collection.JavaConversions._
import com.google.inject.{Injector, Scope, Scopes, Binding}
import com.google.inject.spi.BindingScopingVisitor
import com.coeusweb.mvc.controller.Controller
import com.coeusweb.FrameworkException

/**
 * Register all the {@code Controller} classes from the bindings in a Guice
 * injector.
 */
private object GuiceRegistrar {
  
  def registerControllers(registry: ControllerRegistry, injector: Injector) {
    for {
      (key, binding) <- injector.getBindings
      controllerClass = key.getTypeLiteral.getRawType
      if classOf[Controller].isAssignableFrom(controllerClass)
    } {
      assertControllerScope(binding)
      registry.controllers += injector.getInstance(controllerClass).asInstanceOf[Controller]
    }
  }
  
  private def assertControllerScope(b: Binding[_]) {
    if (b.acceptScopingVisitor(hasScope))
      throw new FrameworkException("The Guice binding: " + b +
        " has wrong scope. Controller bindings must have the Singleton scope.")
  }
  
  private object hasScope extends BindingScopingVisitor[Boolean] {
    def visitNoScoping = true
    def visitEagerSingleton = false
    def visitScope(scope: Scope) = scope != Scopes.SINGLETON
    def visitScopeAnnotation(ann: Class[_ <: Annotation]) = ann != classOf[Singleton] 
  }
}
