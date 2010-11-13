/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.bean

import java.util.Locale
import com.coeusweb.bind.{ Error, ErrorFormatter, ConverterRegistry }
import com.coeusweb.i18n.msg.MessageBundle

/**
 * The error formatter of {@code BeanValidator}. 
 */
object BeanErrorFormatter extends ErrorFormatter {

  /**
   * Formats the message of the specified error.
   * 
   * <p>Binding error messages are loaded from the given {@code MessageBundle}.
   * Validation error messages are pre-loaded by the {BeanValidator} from the
   * JSR-303 validation provider specific files.</p>
   */
  def format(error: Error, locale: Locale, messages: MessageBundle, formatters: ConverterRegistry) = {
    if (Error.isTypeMismatch(error)) {
      messages(locale, error.code, error.args)
    } else {
      error.code
    }
  }
}