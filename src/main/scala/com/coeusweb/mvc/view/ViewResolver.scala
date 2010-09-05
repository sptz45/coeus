/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.view

/**
 * Maps view names (returned from {@code Controller} handler methods) to {@code View}
 * instances that will be used to render a response.
 * 
 * @see View
 */
trait ViewResolver {
  
  /**
   * Returns a view corresponding to the specified name.
   * 
   * @param name the name of the view
   * @return a {@code View} to render the request or {@code null}
   *          if a view cannot get resolved using the specified name.
   */
  def resolve(name: String): View
}
