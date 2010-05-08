/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import java.net.URI
import java.util.Locale
import com.coeusweb.bind.Converter

class UriConverter extends Converter[URI] {
  
  def parse(text: String, locale: Locale) = new URI(text)
  
  def format(uri: URI, locale: Locale) =
    if (uri eq null) "" else uri.toString
}
