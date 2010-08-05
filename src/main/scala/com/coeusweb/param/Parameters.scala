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
  
  private def readParameter(name: String): String = {
    val param = getParameter(name)
    if (param eq null) return null
    val trimmed = param.trim
    if (trimmed == "") null else trimmed
  }
  
  /**
   * Test whether the parameter with the specified name exists.
   * 
   * @param name the name or the parameter.
   * @return {@code true} if the parameter with the specified name exists, else {@code false}
   */
  def contains(name: String) = getParameter(name) ne null
  
  /**
   * Retrieve the parameter with the specified name.
   * 
   * <p>The parameter value gets trimmed before it gets returned. Furthermore
   * if the parameter value is the empty string (after trimming) then we assume
   * that the parameter does not exist.</p>
   * 
   * @param name the name of the parameter
   * 
   * @throws MissingParameterException if a parameter with the specified name does not exist.
   * @return the parameter's value
   */
  def apply(name: String): String = {
    val parameter = readParameter(name)
    if (parameter eq null)
      throw new MissingParameterException(name)
    parameter
  }
  
  /**
   * Retrieve the parameter with the specified name.
   * 
   * <p>The parameter value gets trimmed before it gets returned. Furthermore
   * if the parameter value is the empty string (after trimming) then we assume
   * that the parameter does not exist.</p>
   * 
   * @param name the name of the parameter
   * 
   * @return an <code>Option</code> containing the parameter value if
   *         the parameter exists.
   */
  def get(name: String): Option[String] = Option(readParameter(name))
  
  /**
   * Retrieve the parameter with the specified name.
   * 
   * <p>The parameter value gets trimmed before it gets returned. Furthermore
   * if the parameter value is the empty string (after trimming) then we assume
   * that the parameter does not exist.</p>
   * 
   * @param T the type to convert this parameter to
   * @param name the name of the parameter
   * 
   * @return an <code>Option</code> containing the parameter value converted to
   *         {@code T} if the parameter exists.
   */
  def parse[T](name: String)(implicit m: Manifest[T]): Option[T] = {
    val parameter = readParameter(name)
    if (parameter eq null) None
    else Some(parseValue(m, parameter, null))
  }
  
  /**
   * Retrieve the parameter with the specified name.
   * 
   * <p>The parameter value gets trimmed before it gets returned. Furthermore
   * if the parameter value is the empty string (after trimming) then we assume
   * that the parameter does not exist.</p>
   * 
   * @param T the type to convert this parameter to
   * @param name the name of the parameter
   * @param parser the <code>Parser</code> to use for converting the parameter to <code>T</code>
   * 
   * @return an <code>Option</code> containing the parameter value converted to
   *         {@code T} if the parameter exists.
   */
  def parse[T](name: String, parser: Parser[T])(implicit m: Manifest[T]): Option[T] = {
    val parameter = readParameter(name)
    if (parameter eq null) None else Some(parseValue(m, parameter, parser))
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
