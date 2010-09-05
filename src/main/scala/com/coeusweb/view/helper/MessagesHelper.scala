/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view.helper

import com.coeusweb.i18n.msg.MessageBundle
import com.coeusweb.ScopeAccessor

/**
 * A view helper to read i18n messages.
 */
trait MessagesHelper {

  /**
   * Get the message for the specified code using the {@code Locale} retrieved from
   * the configured {@code LocaleResolver}.
   * 
   * @param code the code of the message 
   * @param args the arguments to use for substituting any variables in the message
   * 
   * @throws MessageNotFoundException if a message does not exist for the
   *         specified code and resolved {@code Locale}.
   */
  def msg(code: String, args: Any*)(implicit scopes: ScopeAccessor) = {
    val request = scopes.request
    request.messages(request.locale, code, args)
  }
}
