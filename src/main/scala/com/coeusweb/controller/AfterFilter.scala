/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.controller

import com.coeusweb.Controller

/**
 * Executes common logic <em>after</em> each handler method of a
 * {@code Controller}.
 * 
 * @see Controller
 * @see {@link com.coeusweb.core.Handler#handle Handler.handle()}
 */
trait AfterFilter {
  
  this: Controller =>

  /**
   * Gets executed <em>after</em> the {@code Controller}'s handler method that
   * corresponds to the current request.
   * 
   * @param error may contain a {@code Throwable} if an uncaught exception
   *        occurred during the execution of the {@code Controller}'s handler
   *        method. 
   */
  def after(error: Option[Throwable])
}
