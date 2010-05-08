/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 * 
 * Ported from Spring Framework, original authors
 * are Juergen Hoeller and Jean-Pierre Pawlak.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.i18n.locale

import java.util.Locale
import javax.servlet.http._
import com.coeusweb.bind.converter.LocaleConverter

/**
 * Resolves the user's locale based on an HTTP cookie.
 * 
 * <p>This implementation caches the resolved Locale in a ServletRequest attribute.</p>
 * 
 * @param default the <code>Locale</code> to use if a cookie with a locale is not found
 * in the user's request.
 */
class CookieLocaleResolver(default: Option[Locale] = None) extends LocaleResolver {
  
  private[this]   val ATTRIBUTE_NAME = classOf[CookieLocaleResolver].getName + ".Locale"
  private[locale] val COOKIE_NAME = classOf[CookieLocaleResolver].getSimpleName + ".Locale"
  
  private val parseLocale = new LocaleConverter().parse(_: String, null)
  
  /**
   * Get the user's locale from an HTTP cookie.
   * 
   * <p>If an HTTP cookie with the locale is not found in the user's request then the 
   * default locale is returned. If the default locale is not specified then the locale from
   * the <code>HttpServletRequest</code> is returned.</p>
   * 
   * @param request the request that contains the cookie to use for resolving the Locale
   */
  def resolve(request: HttpServletRequest): Locale = {
    var locale = request.getAttribute(ATTRIBUTE_NAME).asInstanceOf[Locale]
    if (locale ne null) {
      return locale
    }
    if (request.getCookies eq null) {
      return defaultLocaleFor(request)
    }
    for(cookie <- request.getCookies.find(_.getName == COOKIE_NAME)) {
      locale = parseLocale(cookie.getValue)
      request.setAttribute(ATTRIBUTE_NAME, locale)
      return locale
    }
    defaultLocaleFor(request)
  }
  
  
  /**
   * Set the specified locale in a HTTP cookie.
   * 
   * <p>If the specified <code>Locale</code> is <code>null</code> then the cookie with the locale
   * will get removed (its max-age will be set to zero) and this resolver will return the default
   * locale in subsequent calls of the {@link #resolve(request)} method</p>
   * 
   * @param request the current Servlet request
   * @param response the current Servlet response
   * @param locale the user's locale
   */
  def setLocale(request: HttpServletRequest, response: HttpServletResponse, locale: Locale) {    
    if (locale ne null) {
      request.setAttribute(ATTRIBUTE_NAME, locale)
      val cookie = new Cookie(COOKIE_NAME, locale.toString)
      cookie.setMaxAge(Integer.MAX_VALUE)
      cookie.setPath(request.getServletPath)
      response.addCookie(cookie)
    } else {
      request.setAttribute(ATTRIBUTE_NAME, defaultLocaleFor(request))
      val cookie = new Cookie(COOKIE_NAME, "")
      cookie.setMaxAge(0)
      cookie.setPath(request.getServletPath)
      response.addCookie(cookie)
    }
  }
  
  
  private def defaultLocaleFor(request: HttpServletRequest) = default match {
    case None => request.getLocale
    case Some(locale) => locale
  }
}
