/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.view
package scalate

import scala.collection.JavaConversions._
import org.fusesource.scalate._
import com.coeusweb.mvc.{ WebRequest, WebResponse }
import com.coeusweb.mvc.scope.ScopeAccessor

class ScalateView(engine: CustomTemplateEngine, template: Template) extends View {
  
  def contentType: String = "text/html"
  
  def render(request: WebRequest, response: WebResponse) {    
    val context = new CustomRenderContext(engine, request, response.writer)
    
    addDefaultAttributes(context.attributes)
    context.attributes("scopes") = ScopeAccessor(request, response)
    processAttributes(context.attributes)
    
    ViewUtil.applyContentType(contentType, response)
    engine.layout(template, context)
  }
  
  private def addDefaultAttributes(attributes: AttributeMap[String, Any]) {
    for ((attr, value) <- engine.bindingAttributes)
      attributes(attr) = value
  }
  
  def processAttributes(attributes: AttributeMap[String, Any]) { }
}
