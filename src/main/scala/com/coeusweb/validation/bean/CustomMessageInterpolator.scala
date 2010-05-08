/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.bean

import java.util.Locale
import javax.validation.MessageInterpolator
import MessageInterpolator.Context
import com.coeusweb.i18n.locale.LocaleHolder

class CustomMessageInterpolator(interpolator: MessageInterpolator) extends MessageInterpolator {

  def interpolate(messageTemplate: String, context: Context): String = {
    interpolator.interpolate(messageTemplate, context, LocaleHolder.locale)
  }

  def interpolate(messageTemplate: String, context: Context, locale: Locale): String = {
    interpolator.interpolate(messageTemplate, context, LocaleHolder.locale)
  }
}