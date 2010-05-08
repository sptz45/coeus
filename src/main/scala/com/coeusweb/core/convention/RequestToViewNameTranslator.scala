/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.convention

import com.coeusweb.WebRequest

/**
 * Provides a way to derive a view name from a given request.
 * 
 * <p>This is useful for convention-over-configuration, when we want to
 * allow the user to omit specifying a view name from the controller method
 * and have the framework come up with an appropriate new name.</p>
 *
 * @see {@link com.coeusweb.Controller Controller}
 * @see {@link com.coeusweb.view.ViewResolver ViewResolver}
 */
object RequestToViewNameTranslator {

  /**
   * Get a view name from the request URI of the specified request.
   * 
   * <p>The file extension and any leading and trailing slashes get removed.
   * Also the view name for the root URI "/" is "index".</p>
   */
  def viewNameForRequest(request: WebRequest): String = {
    def dropSlashes(s: String) = {
      var tmp = s
      if (s.charAt(0) == '/') tmp = tmp.substring(1)
      if (s.charAt(s.length - 1) == '/') tmp = tmp.substring(0, tmp.length - 1)
      tmp
    }
    def dropFileExtension(s: String) = {
      if (s.contains(".") && s.charAt(s.length - 1) != '/') s.substring(0, s.lastIndexOf("."))
      else s
    }
    
    val uri = request.requestUri
    if (uri == "/") return "index"
    dropSlashes(dropFileExtension(uri))
  }
}