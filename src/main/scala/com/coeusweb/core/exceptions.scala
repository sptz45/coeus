/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb
package core

/**
 * Thrown when a <code>Controller<code> class has invalid structure.
 * 
 * @see {@link com.coeusweb.Controller Controller}
 */
class InvalidControllerClassException(message: String) extends FrameworkException(message)