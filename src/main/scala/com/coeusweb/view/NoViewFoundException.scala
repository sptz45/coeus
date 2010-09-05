/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view

import com.coeusweb.FrameworkException

/**
 * To be thrown from the framework when a {@code View} cannot get resolved using
 * a particular name.
 * 
 * @see ViewResolver
 */ 
class NoViewFoundException(viewName: String)
  extends FrameworkException("Could not find a view with name: '"+viewName+"'")
