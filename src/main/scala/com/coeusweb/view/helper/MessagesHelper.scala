/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view.helper

import com.coeusweb.i18n.msg.MessageBundle

/**
 * A view helper to read i18n messages.
 */
trait MessagesHelper {
  
  val messages: MessageBundle
  
  /**
   * Get a message for the given code using the <code>Locale</code> for the
   * configured <code>LocaleResolver</code>.
   * 
   * @param code the code of the message 
   * @param args the arguments to use for substituting any variables in the message
   * 
   * @throws MessageNotFoundException if a message does not exist for the
   *         specified code and resolved Locale.
   */
  def msg(code: String, args: Any*)(implicit scopes: ScopeAccessor) = {
    messages(scopes.request.locale, code, args)
  }

}