/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.i18n.msg

import java.util.Locale
import com.tzavellas.coeus.FrameworkException
import com.tzavellas.coeus.util.internal.Strings.nullSafeToString

/**
 * Thrown when a message could get found in a <code>MessageBundle</code> using
 * the given <code>messageCode</code> and <code>Locale</code>.
 */
class MessageNotFoundException(messageCode: String, locale: Locale, cause: Throwable)
  extends FrameworkException(
    "Could not find message using code: '%s' for locale: '%s'"
      .format(messageCode, nullSafeToString(locale)), cause)
