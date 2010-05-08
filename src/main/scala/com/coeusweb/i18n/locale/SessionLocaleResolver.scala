/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.i18n.locale

import java.util.Locale
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }

/**
 * Resolves the user's locale based on an attribute in <code>HttpSession</code>.
 * 
 * @param default the <code>Locale</code> to use if one is not found in the
 * <code>HttpSession</code>.
 */
class SessionLocaleResolver(default: Option[Locale] = None) extends LocaleResolver {
  
  private[this] val ATTRIBUTE_NAME = classOf[SessionLocaleResolver].getName + ".Locale"

  /**
   * Get the user's locale from <code>HttpSession</code>.
   * 
   * <p>If the attribute with the locale is not found in the <code>HttpSession</code> the
   * default locale is returned. If the default locale is not specified then the locale from
   * the <code>HttpServletRequest</code> is returned.</p>
   * 
   * @param request the request to use for resolving the Locale
   */
  def resolve(request: HttpServletRequest) = {
    
    def defaultLocale = default match {
      case None         => request.getLocale
      case Some(locale) => locale
    }
    
    val session = request.getSession(false)
    if (session eq null) {
      defaultLocale
    } else {
      val locale = session.getAttribute(ATTRIBUTE_NAME)
      if (locale eq null) defaultLocale
      else locale.asInstanceOf[Locale]
    }
  }
    

  /**
   * Set the specified locale in the <code>HttpSession</code>.
   * 
   * <p>If the specified <code>Locale</code> is <code>null</code> then the locale's attribute
   * will get removed from <code>HttpSession</code> and this resolver will return the default
   * locale in subsequest calls of the {@link #resolve(request)} method.</p>
   * 
   * @param request the current Servlet request
   * @param response the current Servlet response
   * @param locale the user's locale
   */
  def setLocale(request: HttpServletRequest, response: HttpServletResponse, locale: Locale) {
    if (locale eq null) {
      request.getSession.removeAttribute(ATTRIBUTE_NAME)
    } else {
      request.getSession.setAttribute(ATTRIBUTE_NAME, locale)
    } 
  }
}