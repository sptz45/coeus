/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.config

private object ConventionUtils {

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