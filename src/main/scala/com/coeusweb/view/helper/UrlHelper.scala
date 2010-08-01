/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view.helper

import javax.servlet.ServletContext

/**
 * Helper methods for creating URLs.
 */
trait UrlHelper {

  /** The {@code ServletContext} of the web application. */
  val servletContext: ServletContext
  
  /**
   * Prepends the ContextPath to the specified path.
   * 
   * @param path the input path (must not be empty)
   * @see {@link javax.servlet.ServletContext#getContextPath ContextPath}
   */
  def url(path: String) = {
    require(path.length > 0)
    
    def makeAbsolute(path: String) = 
      if (path.charAt(0) == '/') path else "/" + path
    
    val context = servletContext.getContextPath
    if (context == "" || context == "/") makeAbsolute(path)
    else context + makeAbsolute(path)
  }
}
