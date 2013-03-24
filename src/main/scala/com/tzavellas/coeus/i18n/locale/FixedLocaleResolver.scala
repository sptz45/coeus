/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.i18n.locale

import java.util.Locale
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

/**
 * A <code>LocaleResolver</code> that resolves to the same locale for all
 * requests.
 * 
 * @param default the default locale to use if a locale is not set
 */
class FixedLocaleResolver(val default: Locale) extends LocaleResolver {

  @volatile
  private[this] var locale: Locale = null
  
  /**
   * Returns the same locale for all requests.
   */
  def resolve(request: HttpServletRequest) = if (locale eq null) default else locale
  
  /**
   * Sets the locale to be used for all requests.
   */
  def setLocale(request: HttpServletRequest, response: HttpServletResponse, newLocale: Locale) {
    locale = newLocale
  }
}
