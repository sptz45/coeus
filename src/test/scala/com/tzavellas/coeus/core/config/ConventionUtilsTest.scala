/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core.config

import org.junit.Test
import org.junit.Assert._

class ConventionUtilsTest {
  
  import ConventionUtils._

  @Test
  def package_name_to_path() {
    assertEquals("/", packageNameToPath(""))
    assertEquals("blog/", packageNameToPath("blog"))
    assertEquals("blog/comments/", packageNameToPath("blog.comments"))
  }
  
  @Test
  def package_name_to_path_excluding_roots() {
    assertEquals("blog/", packageNameToPath(Nil, "blog"))
    assertEquals("blog/", packageNameToPath(List("com.example"), "com.example.blog"))
    assertEquals("blog/", packageNameToPath(List("com.example"), "blog"))
  }
}