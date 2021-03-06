/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc.view
package scalate

import org.fusesource.scalate._
import com.tzavellas.coeus.mvc.{ WebRequest, WebResponse }
import com.tzavellas.coeus.mvc.scope.ScopeAccessor

private class ScalateView(
  engine: TemplateEngine,
  template: Template,
  attributes: Map[String, Any]) extends View {
  
  def contentType: String = "text/html"
  
  def render(request: WebRequest, response: WebResponse) {    
    val context = new CustomRenderContext(engine, request, response.writer)
    
    attributes foreach { attr => context.attributes(attr._1) = attr._2 }
    context.attributes("scopes") = ScopeAccessor(request, response)
    
    ViewUtil.applyContentType(contentType, response)
    engine.layout(template, context)
  }
}
