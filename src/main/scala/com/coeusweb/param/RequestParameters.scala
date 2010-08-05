/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.param

import java.util.Locale
import javax.servlet.http.HttpServletRequest
import com.coeusweb.bind.ConverterRegistry

/**
 * A class to read Servlet request parameters.
 * 
 * @param servletRequest the current Servlet request
 * @param config the module's configuration
 */
class RequestParameters(
  val servletRequest: HttpServletRequest,
  val locale: Locale,
  val converters: ConverterRegistry) extends Parameters {
  
  def getParameter(name: String) = servletRequest.getParameter(name)
  
  def iterator = new Iterator[(String, String)] {
    val names = servletRequest.getParameterNames.asInstanceOf[java.util.Enumeration[String]]
    def hasNext = names.hasMoreElements
    def next = {
      val name = names.nextElement
      (name -> servletRequest.getParameter(name))
    }
  }
}
