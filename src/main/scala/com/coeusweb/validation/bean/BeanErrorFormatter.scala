/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.bean

import java.util.Locale
import com.coeusweb.bind.{ Error, ErrorFormatter }

class BeanErrorFormatter extends ErrorFormatter {

  def format(error: Error, locale: Locale): String = error.code
}