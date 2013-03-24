/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.validation.bean

import java.util.Locale
import javax.validation.{ Validation => JValidation }

/**
 * Helper methods to bootstrap a JSR-303 validator.
 */
object Validation {

  /**
   * Get the configuration of the default JSR-303 provider and set the
   * interpolator to {@code LocaleAwareInterpolator}. 
   * 
   * @param offlineLocale the offline locale of {@code LocaleAwareInterpolator} 
   * 
   * @return the changed configuration of the default provider
   */
  def defaultConfig(offlineLocale: Locale) = {
     val conf = JValidation.byDefaultProvider.configure()
    conf.messageInterpolator(new LocaleAwareInterpolator(conf.getDefaultMessageInterpolator, offlineLocale))
    conf
  }
}