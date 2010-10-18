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
import com.coeusweb.mvc.WebRequest

/**
 * A {@code MessageInterpolator} implementation that delegates to another
 * interpolator using the locale retrieved from the configured
 * {@code LocaleResolver}.
 * 
 * <p>To configure a {@code BeanValidator} to use this interpolator you can use
 * the {@link Validation} object or the following code:</p>
 * <pre>
 * val conf= Validation.byDefaultProvider.configure()
 * val factory = conf
 *   .messageInterpolator(
 *     new LocaleAwareInterpolator(conf.getDefaultMessageInterpolator))
 *   .buildValidatorFactory()
 * val validator = new BeanValidator(factory.getValidator)
 * </pre>
 * 
 * @see BeanValidator
 * @see Validation
 */
class LocaleAwareInterpolator(
  interpolator: MessageInterpolator,
  offlineLocale: Locale)
    extends MessageInterpolator {

  def interpolate(messageTemplate: String, context: Context) =
    interpolator.interpolate(messageTemplate, context, actualLocale)

  def interpolate(messageTemplate: String, context: Context, locale: Locale) =
    interpolator.interpolate(messageTemplate, context, actualLocale)
  
  private def actualLocale = {
    val req = WebRequest.currentRequest
    if (req eq null) offlineLocale else req.locale
  }
}