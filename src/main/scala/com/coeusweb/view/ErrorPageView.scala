/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view

import com.coeusweb.{ WebRequest, WebResponse }

/**
 * A <code>View</code> instance to be returned from <code>ExceptionHandler</code>
 * implementations.
 * 
 * <p>This <code>View</code> is used to convey to the framework to propagate any
 * uncaught exceptions to the Servlet container.</p> 
 *
 * @see {@link com.coeusweb.core.ExceptionHandler ExceptionHandler}
 * @see {@link com.coeusweb.core.ErrorPageExceptionHandler ErrorPageExceptionHandler}
 */
case object ErrorPageView extends View {
  
  /** The content-type is null. */ 
  def contentType = null
  
  /** Does nothing. */
  def render(request: WebRequest, response: WebResponse) { }
}