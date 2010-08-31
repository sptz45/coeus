/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view.helper

import com.coeusweb.core.util.Interpolator
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
  def url(path: String): String = {
    require(path.length > 0)
    
    def makeAbsolute(path: String) = 
      if (path.charAt(0) == '/') path else "/" + path
    
    val context = servletContext.getContextPath
    if (context == "" || context == "/") makeAbsolute(path)
    else context + makeAbsolute(path)
  }
  
  /**
   * Prepends the ContextPath and performs variable substitution to the
   * specified path.
   * 
   * <p>The variables are delimited by the '{' and '}' characters and their
   * content is ignored. The substitution is based on the position of the
   * specified arguments.</p>
   * 
   * <p>Example: if the context path is <em>/test</em> then invoking
   * <code>url("/space/{sid}/page/{pid}/edit", "development", 42)</code> will
   * produce the path <em>/test/space/development/page/42/</em>.
   * 
   * @param path the input path (must not be empty)
   */
  def url(path: String, args: Any*): String = {
    val interpolated = Interpolator.interpolateVars(path, args)
    url(interpolated)
  }
}
