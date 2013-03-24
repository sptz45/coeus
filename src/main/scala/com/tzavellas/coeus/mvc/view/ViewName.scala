/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc
package view


/**
 * A <code>View</code> that holds a <em>view name</em> that can be used to
 * resolve another <code>View</code> using a <code>ViewResolver</code>. 
 * 
 * @param name the name of the View this view refers to
 * @see ViewResolver
 */
final case class ViewName(name: String) extends View {
  
  /** The content-type is {@code null}. */ 
  def contentType = null
  
  /** Unsupported operation. */
  def render(request: WebRequest, response: WebResponse) {
    throw new UnsupportedOperationException("Called render() on a ViewReference")
  }
}
