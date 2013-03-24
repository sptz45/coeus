/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc
package controller

import com.tzavellas.coeus.http.HttpStatus
import util.Conventions
import view._

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

   * @see {@link com.tzavellas.coeus.mvc.view.ViewResolver ViewResolver}
   */
  def render = new ViewName(Conventions.viewNameForRequest(request)) 
}