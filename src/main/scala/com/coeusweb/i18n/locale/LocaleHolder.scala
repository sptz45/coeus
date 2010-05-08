/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.i18n.locale

import com.coeusweb.WebRequest

/**
 * Holds the locale of the current web request.
 */
object LocaleHolder {
  
  /**
   * The locale of the current web request.
   * 
   * <p>The locale gets resolved using the framework's configured
   * {@code LocaleResolver}.</p>
   */
  def locale = WebRequest.currentRequest.locale
}