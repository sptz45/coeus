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
 * Resolves the user's locale from the primary locale specified in the
 * <i>accept-language</i> header of the HTTP request.
 * 
 * @see HttpServletRequest#getLocale()
 */
class AcceptHeaderLocaleResolver extends LocaleResolver {

  /**
   * Return the locale specified in the <em>accept-language</em> header
   * of the HTTP request.
   */
  def resolve(request: HttpServletRequest) = request.getLocale
  
  /**
   * This <code>LocaleResolver</code> always uses the locale specified in the
   * <i>accept-language</code> header of the HTTP request so this method is not
   * supported.
   * 
   * @throws UnsupportedOperationException
   */
  def setLocale(request: HttpServletRequest, response: HttpServletResponse, locale: Locale) {
    throw new UnsupportedOperationException
  }
}