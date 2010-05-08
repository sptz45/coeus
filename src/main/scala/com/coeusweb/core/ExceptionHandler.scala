/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import com.coeusweb.view.View

/**
 * A handler that gets called when an uncaught exception occurs during
 * request execution.
 * 
 * <p>Instances of this class must be thread-safe.</p>
 */
trait ExceptionHandler {

  /**
   * Handle the error that occurred during the execution of the specified
   * request.
   * 
   * <p>Note that the returned <code>View</code> might not get used if
   * a response is already sent to the client.</p>
   * 
   * @param context the context of the current request
   * @return a view that will be used to render the response
   */
  def handle(context: RequestContext): View
}
