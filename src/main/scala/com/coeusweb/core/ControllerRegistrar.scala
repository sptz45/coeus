/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import com.coeusweb.Controller
import com.coeusweb.config._
import util.ReflectionHelper


private class ControllerRegistrar(config: DispatcherConfig) {
  
  private val extractor = new HandlerMappingExtractor(config.classNameTranslator, config.methodNameTranslator)

  /**
   * Register the specified controller classes.
   * 
   * @param controllerClasses the controller classes to register
   */
  def registerAll(controllerClasses: Seq[Class[_ <: Controller]]) {
    controllerClasses foreach { registerController(_) }
  }

  /**
   * Extracts any handler mappings from the specified controller class
   * and registers those mapping in the request resolver.
   * 
   * <p>If the specified class is abstract this method does nothing.</p>
   * 
   * @throws InvalidControllerClassException if the controller class has invalid structure
   *         or if it doesn't have handler annotations.
   */
  private def registerController(controllerClass: Class[_ <: Controller]) {
    if (ReflectionHelper.isAbstract(controllerClass)) return
    
    // extract any handler mappings from the annotated methods
    val mappings = extractor.extract(controllerClass)
    
    // register the mappings in request resolver
    for (mapping <- mappings) {
      config.requestResolver.register(
        mapping.path,
        mapping.httpMethod,
        new Handler(controllerClass, mapping.controllerMethod))
    }
    // tell the controller factory about the new controller
    config.controllerFactory.controllerRegistered(controllerClass)
  }
}
