package com.coeusweb.validation.bean

import java.util.Locale
import javax.validation.{ Validation => JValidation }

object Validation {

  def defaultConfig(offlineLocale: Locale) = {
     val conf = JValidation.byDefaultProvider.configure()
    conf.messageInterpolator(new LocaleAwareInterpolator(conf.getDefaultMessageInterpolator, offlineLocale))
    conf
  }
}