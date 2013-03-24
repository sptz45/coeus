/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc.view
package scalate

import javax.servlet.ServletContext
import org.fusesource.scalate._

class ScalateViewResolver(config: ScalateConfigurator) extends ViewResolver {
  
  def this(servlets: ServletContext) = this(new ScalateConfigurator(servlets))
  
  private val prefix = config.templatePrefix
  private val suffix = config.templateSuffix
  
  private val engine       = TemplateEngineFactory.newEngine(config)
  private val attributes   = config.attributes.toMap

  final def resolve(name: String): View = {
    try {
      
      val template = engine.load(prefix + name + suffix)
      new ScalateView(engine, template, attributes)
    
    } catch {
      case e: ResourceNotFoundException => null
    }
  }
}
