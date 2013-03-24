/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core.error

import com.tzavellas.coeus.mvc.{ WebRequest, WebResponse }
import com.tzavellas.coeus.mvc.view.View

/**
 * A <code>View</code> instance to be returned from <code>ExceptionHandler</code>
 * implementations.
 * 
 * <p>This <code>View</code> is used to convey to the framework to propagate any
 * uncaught exceptions to the Servlet container.</p> 
 *
 * @see ExceptionHandler
 */
case object ErrorPageView extends View {
  
  /** The content-type is null. */ 
  def contentType = null
  
  /** Does nothing. */
  def render(request: WebRequest, response: WebResponse) { }
}