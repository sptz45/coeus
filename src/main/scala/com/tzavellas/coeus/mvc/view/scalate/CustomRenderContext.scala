/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc.view.scalate

import java.io.PrintWriter
import org.fusesource.scalate._
import com.tzavellas.coeus.mvc.WebRequest


class CustomRenderContext(
  engine: TemplateEngine,
  request: WebRequest,
  writer: PrintWriter)
    extends DefaultRenderContext(request.uri, engine, writer) {
  
  viewPrefixes = List("WEB-INF/views")
  
  override def locale = request.locale
  
  override val attributes = new AttributeMap {
    update("context", CustomRenderContext.this)
    
	def get(key: String): Option[Any] = Option(apply(key))

    def apply(key: String): Any = request.getAttribute(key)
    
    def keySet = request.attributeNames.toSet

    def update(key: String, value: Any) {
      if (value == null) request -= key
      else request(key) = value
    }

    def remove(key: String): Option[Any] = {
      val answer = get(key)
      request -= key
      answer
    }
  }
}
