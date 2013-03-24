/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc
package view

/**
 * A <i>null-object</i> implementation for {@code View}.
 */
object NullView extends View {
  
  /** The content-type is null. */
  def contentType: String = null
  
  /** Does nothing. */
  def render(request: WebRequest, response: WebResponse) { }
}
