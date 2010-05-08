/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view.helper

import javax.servlet.ServletContext

trait UrlHelper {

  val servletContext: ServletContext
  
  def url(path: String) = {
    val context = servletContext.getContextPath
    if (context == "" || context == "/") path
    else context + "/" + path
  }
}
