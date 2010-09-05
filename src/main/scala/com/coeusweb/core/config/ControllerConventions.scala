/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.config

import java.lang.reflect.Method
import com.coeusweb.core.util.Strings

/**
 * Translator functions for configuring the generation of paths from
 * a controller class.
 */
object ControllerConventions {
  
  /**
   * Always returns the empty string.
   */
  def ignoreMethodName = { m: Method => "" }
  
  /**
   * Return the method name without any transformation.
   */
  def useMethodName = { m: Method => m.getName }
  
  /**
   * Transforms any camel case into lower case words separated with dashes.
   */
  def useMethodNameWithDashes = { m: Method => Strings.camelCaseToDashed(m.getName) }

  /**
   * Always returns the empty string. 
   */
  def ignoreClassName = { c: Class[_] => "" }

  /**
   * Removes the "Controller" suffix (if present) from the controller's class name.
   * 
   * <p>Translates the controller's name by:</p>
   * <ol>
   * <li>transforming and prepending the package name</li>
   * <li>transforming the first character of the simple class name to lower case and...</li>
   * <li>by removing the "Controller" suffix if present.</li>
   * </ol>
   * 
   * <p>If the collection of <code>rootPackages</code> is empty then the package name
   * is not prepended to the generated path. Otherwise the package name is transformed
   * into a path by striping the root package from it and by replacing any dots (".")
   * with slashes ("/").</p>
   * 
   * <p>For example a controller class with the name <code>UserRegistrationController</code>
   * if there is no root package specified gets translated to "/userRegistration".</p>
   */
  def useClassName(rootPackages: Traversable[String] = Nil) = { c: Class[_] =>
    translatePackage(rootPackages, c.getPackage.getName) +
    Strings.firstCharToLower(Strings.removeSuffix(c.getSimpleName, "Controller"))
  }
  
  /**
   * Transforms any camel-case into lower-case and separates the words with dashes.
   * 
   * <p>Translates the controller's name by:</p>
   * <ol>
   * <li>transforming and prepending the package name</li>
   * <li>transforming any camel case into lower case words separated with dashes and...</li>
   * <li>by removing the "Controller" suffix if present.</li>
   * </ol>
   * 
   * <p>If the collection of <code>rootPackages</code> is empty then the package name
   * is not prepended to the generated path. Otherwise the package name is transformed
   * into a path by striping the root package from it and by replacing any dots (".")
   * with slashes ("/").</p>
   * 
   * <p>For example a controller class with the name <code>UserRegistrationController</code>
   * if there is no root package specified gets translated to "/user-registration".</p>
   */
  def useClassNameWithDashes(rootPackages: Traversable[String] = Nil) = { c: Class[_] =>
    translatePackage(rootPackages, c.getPackage.getName) +
    Strings.camelCaseToDashed(Strings.removeSuffix(c.getSimpleName, "Controller"))
  }
  
  private def translatePackage(roots: Traversable[String], pkg: String) =
    if (roots.isEmpty) "" else ConventionUtils.packageNameToPath(roots, pkg)
}