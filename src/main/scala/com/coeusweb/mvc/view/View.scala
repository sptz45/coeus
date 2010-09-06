/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc
package view

/**
 * Renders the model into a {@code WebResponse}.
 */
trait View {
  
  /** The content-type of the response. */
  def contentType: String
  
  /**
   * Render the contents of this view to the specified {@code WebResponse}.
   */
  def render(request: WebRequest, response: WebResponse)
}
