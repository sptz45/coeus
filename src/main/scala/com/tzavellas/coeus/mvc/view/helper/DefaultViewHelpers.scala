/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc.view.helper

import javax.servlet.ServletContext

/**
 * The view helpers that are registered by default in <code>DispatcherConfig</code>.
 */
class DefaultViewHelpers(val servletContext: ServletContext)
  extends MessagesHelper
     with AssetsHelper
     with DateHelper
     with FormHelper
     with UrlHelper
