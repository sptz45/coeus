/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc
package controller

import view.View

/**
 * Executes common logic <em>after</em> each handler method of a
 * {@code Controller}.
 * 
 * @see Controller
 * @see {@link com.tzavellas.coeus.core.Handler#handle Handler.handle()}
 */
trait AfterFilter {
  
  this: Controller =>

  /**
   * Gets executed <em>after</em> the {@code Controller}'s handler method that
   * corresponds to the current request.
   *
   * <p>This method can be used to create controller-specific error handlers.</p>
   *
   * @param error may contain an {@code Exception} if an uncaught exception
   *        occurred during the execution of the {@code Controller}'s handler
   *        method.
   *
   * @return may return some {@code View} to override the {@code View} returned by
   *         the controller's handler method or {@code None} to continue with
   *         normal request processing.  
   */
  def after(error: Option[Exception]): Option[View]
}
