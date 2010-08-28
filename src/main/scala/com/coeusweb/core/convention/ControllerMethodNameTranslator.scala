/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.convention

import java.lang.reflect.Method
import com.coeusweb.core.util.Strings

/**
 * Derives a path from a controller method.
 * 
 * <p>Implementations of this trait are used to derive URL mappings
 * from controller classes for convention-over-configuration.</p>
 * 
 * @see {@link com.coeusweb.Controller Controller}
 */
trait ControllerMethodNameTranslator {
  
  /**
   * Translate the given method's name into a path.
   */
  def translate(method: Method): String
}

/**
 * Transforms any camel case into lower case words separated with dashes.
 */
class DashedControllerMethodNameTranslator extends ControllerMethodNameTranslator {
  def translate(method: Method) = Strings.camelCaseToDashed(method.getName) 
}

/**
 * A translator that simply returns the method name without any transformation.
 */
class DefaultControllerMethodNameTranslator extends ControllerMethodNameTranslator {
  def translate(method: Method) = method.getName
}

/**
 * Always returns the empty string.
 */
object NullControllerMethodNameTranslator  extends ControllerMethodNameTranslator {
  def translate(method: Method) = ""
}