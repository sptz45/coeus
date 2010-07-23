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

/**
 * A {@code MessageInterpolator} implementation that delegates to another
 * interpolator using the locale retrieved from the configured {@code LocaleResolver}.
 * 
 * <p>To configure a {@code BeanValidator} to use this interpolator you can use the
 * following code:</p>
 * <pre>
 * val configuration = Validation.byDefaultProvider.configure()
 * val factory = configuration
 *     .messageInterpolator(new LocaleAwareInterpolator(configuration.getDefaultMessageInterpolator))
 *     .buildValidatorFactory()
 * val validator = new BeanValidator(factory.getValidator)
 * </pre>
 * 
 * @see BeanValidator
 */
class LocaleAwareInterpolator(interpolator: MessageInterpolator) extends MessageInterpolator {

  def interpolate(messageTemplate: String, context: Context): String = {
    interpolator.interpolate(messageTemplate, context, LocaleHolder.locale)
  }

  def interpolate(messageTemplate: String, context: Context, locale: Locale): String = {
    interpolator.interpolate(messageTemplate, context, LocaleHolder.locale)
  }
}