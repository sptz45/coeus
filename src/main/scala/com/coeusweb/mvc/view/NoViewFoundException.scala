/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.view

import com.coeusweb.FrameworkException

/**
 * Thrown when a {@code View} cannot get resolved using a particular name.
 * 
 * @param viewName the name of the view that didn't get resolved.
 * 
 * @see ViewResolver
 */ 
class NoViewFoundException(viewName: String)
  extends FrameworkException("Could not find a view with name: '"+viewName+"'")
