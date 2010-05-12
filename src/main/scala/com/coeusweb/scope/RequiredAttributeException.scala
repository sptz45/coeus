/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.scope

import com.coeusweb.core.{ HttpException, FrameworkException }
import com.coeusweb.http.HttpStatus

/**
 * An exception to be thrown when user tries to retrieve an attribute that
 * does not exist.
 *
 * @param attribute the name of the attribute that does not exist.
 * @param scopeClass the class of the scope that did not contain the attribute.
 */
class RequiredAttributeException(val attribute: String, val scopeClass: Class[_], hint: String = "")
  extends FrameworkException("Could not find attribute: '%s' in scope: %s. %s".format(attribute, scopeClass.getSimpleName, hint)) {
}
