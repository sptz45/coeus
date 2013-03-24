/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc.view

import scala.xml.NodeSeq

/**
 * Renders the specified XML as XHTML.
 * 
 * <p>This is achieved by using the "text/html" Content-Type.</p>
 * 
 * @param xml the XML to render as XHTML
 */
class XhtmlView(xml: NodeSeq) extends XmlView(xml, "text/html")
