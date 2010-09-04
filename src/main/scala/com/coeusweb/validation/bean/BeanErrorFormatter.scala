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

object BeanErrorFormatter extends ErrorFormatter {

  def format(error: Error, locale: Locale, messages: MessageBundle, formatters: ConverterRegistry) = {
    if (Error.isTypeMismatch(error)) {
      messages(locale, error.code, error.args)
    } else {
      error.code
    }
  }
}