/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind

import java.util.Locale
import com.coeusweb.i18n.msg.MessageBundle

/**
 * Formats error messages.
 */
trait ErrorFormatter {
  
  /**
   * Returns a formatted, locale specific, error message for the specified
   * {@code Error} instance.
   */
  def format(error: Error, locale: Locale, messages: MessageBundle, formatters: ConverterRegistry): String
}