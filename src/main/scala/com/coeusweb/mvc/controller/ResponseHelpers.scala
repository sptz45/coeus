/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.controller

import java.io.{ File, InputStream }
import scala.xml.NodeSeq
import com.coeusweb.Controller
import com.coeusweb.http.HttpStatus
import com.coeusweb.core.convention.Conventions
import com.coeusweb.view._

trait ResponseHelpers extends ViewFactory {
  
  this: Controller =>
  
  /**
   * Sets the response's status to <em>NOT_MODIFIED (304)</em> and returns a
   * {@code NullView}.
   */
  def notModified = {
    response.status = HttpStatus.NOT_MODIFIED
    NullView
  }
  
  /**
   * Returns the default {@link ViewName} for the current request.

   * @see {@link com.coeusweb.view.ViewResolver ViewResolver}
   */
  def render = new ViewName(Conventions.viewNameForRequest(request)) 
}