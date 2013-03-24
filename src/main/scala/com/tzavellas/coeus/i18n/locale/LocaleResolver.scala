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
 * Provides a way to resolve the user's locale for a given request. 
 */
trait LocaleResolver {

  /**
   * Resolve the user's locale using the specified request.
   * 
   * @param request the request to use for resolving the Locale
   * @return a valid <code>Locale</code> object (never <code>null</code>) 
   */
  def resolve(request: HttpServletRequest): Locale
  
  /**
   * Set the user's locale to the specified <code>Locale</code> object.
   * 
   * @param request the current Servlet request
   * @param response the current Servlet response
   * @param locale the user's locale
   */
  def setLocale(request: HttpServletRequest, response: HttpServletResponse, locale: Locale)
}