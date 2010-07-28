/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind

import scala.util.control.NoStackTrace
import com.coeusweb.core.FrameworkException

/**
 * A root exception for all binding related exceptions.
 */
class BindingException(message: String, cause: Throwable = null)
  extends FrameworkException(message, cause)

/**
 * Thrown from {@code ConverterRegistry} when a {@code Converter} does not
 * exist for a specified class. 
 */
class NoConverterAvailableException(klass: Class[_])
  extends BindingException("No converter available for instances of " + klass.getName)

/**
 * Thrown when an error occurs in {@code ExpressionLanguage}. 
 */
class ExpressionException(message: String, cause: Throwable = null)
  extends BindingException(message, cause)

/**
 * Thrown from {@code ExpressionLanguage} when a {@code Parser} cannot
 * parse an input value.
 */
class ParserException(val outputClass: Class[_], cause: Throwable)
  extends BindingException("Could not convert to " + outputClass, cause)
  with NoStackTrace