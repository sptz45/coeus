/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import com.coeusweb.mvc.controller.Controller
import com.coeusweb.mvc.scope.ApplicationScope
import config.DispatcherConfig

private class ControllerRegistrar(config: DispatcherConfig, app: ApplicationScope) {
  
  private val extractor =
    new HandlerMappingExtractor(config.classNameTranslator,
                                config.methodNameTranslator)
  
  private def requestResolver = config.requestResolver

  /**
   * Register the specified controllers.
   */
  def registerAll(controllers: Seq[Controller]) {
    controllers foreach { registerController(_) }
  }

  private def registerController(controller: Controller) {
    controller.application = app
    controller.messageBundle = config.messageBundle
    controller.converters = config.converters 
    
    // extract any handler mappings from the annotated methods
    val mappings = extractor.extract(controller.getClass)
    
    // register the mappings in request resolver
    for (mapping <- mappings) {
      requestResolver.register(
        mapping.path,
        mapping.httpMethod,
        new Handler(controller, mapping.controllerMethod))
    }
  }
}
