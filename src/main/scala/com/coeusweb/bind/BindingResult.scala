/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind

import java.util.Locale
import scala.collection.mutable.{ Map => MutableMap }
import com.coeusweb.i18n.msg.MessageBundle

/**
 * The result of binding to a target object.
 * 
 * @param T the class of the target object
 * @param converters the Converter used for binding and formatting
 * @param target the target object
 */
class BindingResult[+T <: AnyRef](converters: ConverterRegistry, val target: T) {
    
  private val errorMap = MutableMap[String, Error]()
  
  var errorFormatter: ErrorFormatter = ToStringErrorFormatter
  
  def addError(expr: String, error: Error) {
    errorMap += (expr -> error)
  }
  
  /**
   * Add an error for the specified expression.
   * 
   * @param expr 
   * @param code the message code for the error
   * @param args any arguments to be used for constructing the error message
   */
  def addError(expr: String, code: String, args: Any*) {
    addError(expr, new Error(code, expr, target.getClass, args))
  }
  
  /**
   * Get the errors of this result
   * 
   * @return an {@code Iterable} with all the errors of the result
   *         or an empty {@code Iterable} if there are not errors
   */
  def errors: Iterable[Error] = errorMap.values
  
  /**
   * Tests whether this result has any errors.
   */
  def hasErrors: Boolean = ! errorMap.isEmpty
  
  /**
   * Get the {@code Error} for the specified expression if it exists.
   */
  def error(expr: String): Option[Error] = errorMap.get(expr)
  
  def formatError(expr: String, locale: Locale, messages: MessageBundle) =
    errorMap.get(expr).map(errorFormatter.format(_, locale, null, converters))
  
  def fieldValue(expr: String): Any = ExpressionLanguage.eval(target, expr)
  
  def format(expr: String, locale: Locale) = {
    formatValue(ExpressionLanguage.eval(target, expr), locale)
  }
  
  def formatValue(value: Any, locale: Locale): String = {
    if (value != null)
      converters.formatter(value.asInstanceOf[AnyRef].getClass).format(value, locale)
    else
      ""
  }
}

object ToStringErrorFormatter extends ErrorFormatter {
  def format(error: Error, locale: Locale, messages: MessageBundle, formatters: ConverterRegistry) = 
    error.toString
}