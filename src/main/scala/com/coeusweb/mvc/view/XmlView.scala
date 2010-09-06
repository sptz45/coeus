/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.view

import scala.xml.NodeSeq
import com.coeusweb.mvc.{ WebRequest, WebResponse }

/**
 * Renders the specified XML.
 * 
 * @param xml the XML to render
 * @param contentType the response's content-type
 */
class XmlView(xml: NodeSeq, val contentType: String = "application/xml") extends View {
  
  def render(request: WebRequest, response: WebResponse) {
    ViewUtil.applyContentType(contentType, response)
    response.writer.write(xml.toString)
  }
}
