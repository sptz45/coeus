/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec

import java.util.Locale
import com.coeusweb.bind.{ ConverterRegistry, Error, ErrorFormatter }
import com.coeusweb.i18n.msg.MessageBundle
import com.coeusweb.util.internal.{ Strings, Interpolator }

object VSpecErrorFormatter extends ErrorFormatter { 
  
  def format(error: Error, locale: Locale, messages: MessageBundle, formatters: ConverterRegistry) = {
    
    def interpolate(msg: String, args: Seq[Any]) = {
      def fmt: Any => String = {
        case null          => "null"
        case str: String   => str
        case cls: Class[_] => cls.getSimpleName
        case ref: AnyRef   => formatters.formatter(ref.getClass).format(ref, locale)
        case any: Any      => any.toString
      }
      Interpolator.interpolateNumericVars(msg, args, fmt)
    }

    def errorMessage: String = {
    
      def defaultMsgCode = {
        val perObject = new StringBuilder("constraint")
        perObject.append(".")
        perObject.append(error.code)
        perObject.toString
      }

      def perObjectMsgCode = {
        val className = Strings.firstCharToLower(error.args(1).asInstanceOf[Class[_]].getSimpleName)
        val perObject = new StringBuilder(className)
        perObject.append(".")
        perObject.append(error.args(0))
        perObject.append(".")
        perObject.append(error.code)
        perObject.toString
      }

      val perObjectMsg = messages.get(locale, perObjectMsgCode)
      perObjectMsg.getOrElse(messages(locale, defaultMsgCode))
    }
    
    interpolate(errorMessage, error.args)
  }
}
