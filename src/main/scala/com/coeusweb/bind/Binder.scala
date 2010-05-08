/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind

import java.util.Locale

class Binder(
  var converters: ConverterRegistry,
  restriction: Binder.Restriction = Binder.allowAll) {
  
  def bind[T <: AnyRef](parameters: Iterable[(String, String)], target: T, locale: Locale): BindingResult[T] = {
    require(target != null, "Cannot bind to null!")
    
    val result = new BindingResult(converters, target)
    
    for ((expr, value) <- parameters) {
      if (restriction.canBindTo(expr)) {
        try {
          ExpressionLanguage.bind(target, expr, value, locale, converters)
        } catch {
          case e: ParserException =>
            result.addError(expr, Error.typeMismatch(expr, e.outputClass, value))
        }
      }
    }

    result
  }
}

object Binder {
  
  sealed trait Restriction {
    def canBindTo(name: String): Boolean
  }
  
  def denyVars(vars: String*): Restriction = new Restriction {
    def canBindTo(name: String) = ! vars.exists(name.startsWith(_))
  }
  
  def allowVars(vars: String*): Restriction = new Restriction {
    def canBindTo(name: String) = ! vars.exists(name.startsWith(_))
  }

  def allowAll: Restriction = new Restriction {
    def canBindTo(name: String) = true
  }
}
