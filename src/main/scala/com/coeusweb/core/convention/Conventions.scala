/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.convention

import com.coeusweb.core.util.Strings

/**
 * Utility methods that encode various naming conventions.
 */
object Conventions {
  
  /**
   * Derive an attribute name for objects of the specified class.
   * 
   * @return the class' simple name with the first character changed to lower case.
   */
  def classToAttributeName(c: Class[_]) = Strings.firstCharToLower(c.getSimpleName)
  
  /**
   * Translate the package name into a path name by stripping for the name
   * the specified base packages and by replacing the dots (".") with slashes
   * ("/").
   */
  def packageNameToPath(roots: Traversable[String], pkg: String): String = {
    for (root <- roots if pkg.startsWith(root)) {
      if (pkg.length == root.length) return ""
      else  return packageNameToPath(pkg.substring(root.length + 1))
    }
    return packageNameToPath(pkg)
  }
  
  /**
   * Translate the package name into a path name by replacing the dots (".")
   * with slashes ("/").
   */
  def packageNameToPath(pkg: String): String = pkg.replace(".", "/") + "/"
}
