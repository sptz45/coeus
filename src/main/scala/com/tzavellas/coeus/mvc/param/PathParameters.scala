/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc.param

import java.util.Locale
import javax.servlet.http.HttpServletRequest
import scala.collection.Map
import com.tzavellas.coeus.bind.ConverterRegistry

/**
 * A class to read path variables.
 * 
 * <p>Path variables are values encoded in the request URI that correspond
 * to variables defined in URI templates.</p>
 * 
 * @param servletRequest the current Servlet request
 * @param config the module's configuration
 * @param context a map that takes the parameter's name and return the parameter's value
 */
class PathParameters(
  context: Map[String, String],
  val locale: Locale,
  val converters: ConverterRegistry) extends Parameters {
  
  def getParameter(name: String) = {
    try {
      context(name)
    } catch {
      case e: RuntimeException => null
    }
  }
  
  def iterator = context.iterator
}
