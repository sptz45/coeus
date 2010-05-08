/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view.helper

import com.coeusweb.bind.ErrorFormatter
import com.coeusweb.i18n.msg.MessageBundle
import javax.servlet.ServletContext

/**
 * The view helpers that are registered by default in <code>DispatcherConfig</code>.
 */
class DefaultViewHelpers(
  val servletContext: ServletContext,
  val messages: MessageBundle,
  val errorFormatter: ErrorFormatter) extends MessagesHelper
                                         with AssetsHelper
                                         with FormHelper
                                         with UrlHelper