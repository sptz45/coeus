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
import com.coeusweb.bind.Parser

/**
 * A read-only container for parameters.
 * 
 * <p>Parameters (e.g. Servlet request parameters) have String values
 * and this trait provides data conversion facilities to convert them
 * to more appropriate types when necessary.</p>
 * 
 * @see RequestParameters
 * @see PathParameters
 */
trait Parameters extends Iterable[(String, String)] {
  
  /**
   * The converters to use when converting the parameter values from 
   * String to other types.
   */
  val converters: ConverterRegistry
  
  /** The request's locale. */
  val locale: Locale
  
  /** The current Servlet request. */
  def servletRequest: HttpServletRequest
  
  /**
   * Retrieve the parameter with the specified name.
   * 
   * @param name the name of the parameter
   * 
   * @return the parameter value or null if a parameter with the specified
   *         name does not exist.
   */
  def getParameter(name: String): String
  
  /**
   * Test whether the parameter with the specified name exists.
   * 
   * @param name the name or the parameter.
   * @return {@code true} if the parameter with the specified name exists, else {@code false}
   */
  def contains(name: String) = getParameter(name) ne null
  
  /**
   * Retrieve the parameter the specified name.
   * 
   * @param name the name of the parameter
   * 
   * @throws MissingParameterException if a parameter with the specified name does not exist.
   * @return the parameter's value
   */
  def apply(name: String): String = {
    val parameter = getParameter(name)
    if (parameter eq null)
      throw new MissingParameterException(name)
    parameter
  }
  
  /**
   * Retrieve the parameter with the specified name.
   * 
   * @param name the name of the parameter
   * 
   * @return an <code>Option</code> containing the parameter value
   *          if the parameter exists.
   */
  def optional(name: String) = Option(getParameter(name))
  
  /**
   * Retrieve the parameter with the specified name.
   * 
   * @param T the type to convert this parameter to
   * @param name the name of the parameter
   * 
   * @return an <code>Option</code> containing the parameter value converted to
   *         {@code T} if the parameter exists.
   */
  def get[T](name: String)(implicit m: Manifest[T]): Option[T] = {
    val parameter = getParameter(name)
    if (parameter eq null) None
    else Some(parseValue(m, parameter, null))
  }
  
  /**
   * Retrieve the parameter the specified name.
   * 
   * @param T the type to convert this parameter to
   * @param name the name of the parameter
   * 
   * @throws MissingParameterException if a parameter with the specified name does not exist.
   * 
   * @return the parameter value converted to <code>T</code>.
   */
  def parse[T](name: String)(implicit m: Manifest[T]): T = {
    parseValue(m, apply(name), null)
  }
  
  /**
   * Retrieve the parameter the specified name.
   * 
   * @param T the type to convert this parameter to
   * @param parser the <code>Parser</code> to use for converting the parameter to <code>T</code>
   * @param name the name of the parameter
   * @param default an optional default value
   * 
   * @throws MissingParameterException if a parameter with the specified name does not exist and
   *         <code>default</code> is <code>None</code>.
   * 
   * @return the parameter value converted to <code>T</code> or <code>default</code>
   *         if a parameter with the specified name does not exist and <code>default</code> has a
   *         value of <code>Some</code> else throws <code>MissingParameterException</code>. 
   */
  def parse[T](name: String, parser: Parser[T], default: Option[T] = None)(implicit m: Manifest[T]): T = {
    val parameter = getParameter(name)
    if (parameter eq null)
      default match {
        case None => throw new MissingParameterException(name)
        case Some(value) => value
    } else {
      parseValue(m, parameter, parser)
    }
  }
  
  
  private def parseValue[T](m: Manifest[T], parameter: String, parserOrNull: Parser[T]): T = {
    
    val resultClass = m.erasure
    
    /* If no type argument is specified (manifest is for java.lang.Object),
     * return the parameter without conversion. */
    if (classOf[Object] == resultClass)
      return parameter.asInstanceOf[T]
    
    /* Parse the parameter using an appropriate parser for its target type. */
    val parser = if (parserOrNull eq null) converters.converter(resultClass) else parserOrNull
    parser.parse(parameter, locale).asInstanceOf[T]
  }
}
