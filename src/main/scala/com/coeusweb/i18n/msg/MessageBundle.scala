/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.i18n.msg

import java.util.Locale

/**
 * A bundle of locale-specific messages.
 * 
 * @see PropertiesMessageBundle
 */
trait MessageBundle {
  
  /**
   * Get a message for the given code and Locale.
   * 
   * @param locale the locale of the message
   * @param code the code of the message 
   * @param args the arguments to use for substituting any variables in the message
   * 
   * @throws MessageNotFoundException if a message does not exist for the
   *         specified code and Locale.
   */
  def apply(locale: Locale, code: String, args: Any*): String
  
  /**
   * Get a message for the given code and Locale.
   * 
   * @param locale the locale of the message
   * @param code the code of the message 
   * @param args the arguments to use for substituting any variables in the message
   */
  def get(locale: Locale, code: String, args: Any*): Option[String]
}
