/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core

import com.tzavellas.coeus.FrameworkException

/**
 * Thrown when a <code>Controller<code> class has invalid structure.
 * 
 * @see {@link com.tzavellas.coeus.mvc.controller.Controller Controller}
 */
class InvalidControllerClassException(message: String) extends FrameworkException(message)