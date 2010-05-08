/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view
package scalate

import java.util.Locale
import javax.servlet.ServletContext
import org.fusesource.scalate._

class ScalateViewResolver(
  servletContext: ServletContext,
  config: ScalateConfig = new ScalateConfig)
    extends ViewResolver {
  
  private val prefix = config.templatePrefix
  private val suffix = config.templateSuffix
  private val engine = new CustomTemplateEngine(servletContext)
  engine.configure(config)

  final def resolve(name: String): View = {
    try {
      val template = engine.load(prefix + name + suffix)
      new ScalateView(engine, template)
    } catch {
      case e: ResourceNotFoundException => null
    }
  }

  /**
   * Factory method for ScalateView objects.
   * 
   * <p>Subclasses can override this method to create a customized
   * <code>ScalateView</code>.</p>
   */
  protected def createView(engine: CustomTemplateEngine, template: Template): ScalateView =
    new ScalateView(engine, template)
}
