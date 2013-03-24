/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc
package view

/**
 * Renders the specified String.
 * 
 * @param contents the String to render
 * @param contentType the response's content-type 
 */
class TextView(contents: String, val contentType: String) extends View {
  
  def render(request: WebRequest, response: WebResponse) {
    ViewUtil.applyContentType(contentType, response)
    response.writer.write(contents)
  }
}
