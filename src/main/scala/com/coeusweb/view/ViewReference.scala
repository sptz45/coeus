/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view

import com.coeusweb.{ WebRequest, WebResponse }

/**
 * This <code>View</code> serves as a reference to a <code>View</code> that will get
 * resolved by a <code>ViewResolver</code> using the specified view name.
 * 
 * @param name the name of the View this view refers to
 * @see ViewResolver
 */
final case class ViewReference(name: String) extends View {
  
  /** The content-type is {@code null}. */ 
  def contentType = null
  
  /** Unsupported operation. */
  def render(request: WebRequest, response: WebResponse) {
    throw new UnsupportedOperationException("Called render() on a ViewReference")
  }
}