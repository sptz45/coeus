/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.convention

import com.coeusweb.core.util.Strings

/**
 * Derives a base path from the class name of a controller.
 * 
 * <p>Implementations of this trait are used to derive URL mappings
 * from controller classes for convention-over-configuration.</p>
 * 
 * @see {@link com.coeusweb.Controller Controller}
 */
trait ControllerClassNameTranslator {
  
  /**
   * Translate the given class into a base path.
   */
  def translate(controller: Class[_]): String 
}

/**
 * Always returns the empty string.
 */
object NullControllerClassNameTranslator extends ControllerClassNameTranslator {
  def translate(controller: Class[_]) = ""
}

/**
 * An abstract <code>ControllerClassNameTranslator</code> that prepends the
 * package name and delegates to subclasses for formating the simple class name.
 * 
 * <p>If the list of <code>rootPackages</code> is empty then the package name is
 * not prepended to the generated path.
 * Otherwise the package name is transformed into a path by striping any base package from
 * it and by replacing any dots (".") with slashes ("/").</p> 
 * 
 * @see Conventions#packageNameToPath
 */
abstract class AbstractControllerClassNameTranslator(rootPackages: Iterable[String])
  extends ControllerClassNameTranslator {
  
  final def translate(controller: Class[_]): String = 
    translatePackage(controller.getPackage.getName) + translateSimpleName(controller.getSimpleName)
  
  private def translatePackage(pkg: String) =
    if (rootPackages.isEmpty) "" else Conventions.packageNameToPath(rootPackages, pkg) 
  
  /**
   * Translate the simple name of the given class to a path.
   */
  protected def translateSimpleName(className: String): String
}

/**
 * Transforms any camel-case into lower-case and separates the words with dashes.
 * 
 * <p>Translates the controller's name by:</p>
 * <ol>
 * <li>prepending the transformed package according to the superclass</li>
 * <li>transforming any camel case into lower case words separated with dashes and...</li>
 * <li>by removing the "Controller" suffix if present.</li>
 * </ol>
 * 
 * <p>For example a controller class with the name <code>UserRegistrationController</code>
 * if there is no root package specified gets translated to "/user-registration".</p> 
 * 
 * @see AbstractControllerClassNameTranslator
 */
class DashedControllerClassNameTranslator(rootPackages: Iterable[String])
  extends AbstractControllerClassNameTranslator(rootPackages) {
  
  protected def translateSimpleName(className: String) =
    Strings.camelCaseToDashed(Strings.removeSuffix(className, "Controller")) 
}

/**
 * A translator that simply removes the "Controller" suffix (if present)
 * from the controller's class name.
 * 
 * <p>Translates the controller's name by:</p>
 * <ol>
 * <li>prepending the transformed package according to the superclass</li>
 * <li>transforming the first character of the simple class name to lower case and..</li>
 * <li>by removing the "Controller" suffix if present.</li>
 * </ol>
 * 
 * <p>For example a controller class with the name <code>UserRegistrationController</code>
 * if there is no root package specified gets translated to "/userRegistration".</p>
 * 
 * @see AbstractControllerClassNameTranslator
 */
class DefaultControllerClassNameTranslator(rootPackages: Iterable[String])
  extends AbstractControllerClassNameTranslator(rootPackages) {
  
  protected def translateSimpleName(className: String) =
    Strings.firstCharToLower(Strings.removeSuffix(className, "Controller"))
}
