/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.scope.support

import javax.servlet.http.{ HttpSessionListener, HttpSessionEvent }
import com.coeusweb.scope.WebSession

/**
 * Adds a session attribute, that can be safely used as
 * a mutex to synchronize access on the <code>HttpSession</code>.
 * 
 * @see WebSession
 * @see CheckAndActOperations
 */
class HttpSessionMutexListener extends HttpSessionListener {

  /**
   * Adds a mutex object to the newly created HttpSession.
   */
  def sessionCreated(e: HttpSessionEvent) {
    WebSession.setupMutex(e.getSession)
  }
  
  def sessionDestroyed(e: HttpSessionEvent) { }
}
