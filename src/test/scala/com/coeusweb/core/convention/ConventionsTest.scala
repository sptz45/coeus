/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.convention

import org.junit.Test
import org.junit.Assert._

class ConventionsTest {

  @Test
  def class_to_attribute_name() {
    assertEquals("string", Conventions.classToAttributeName(classOf[String]))
    assertEquals("threadLocal", Conventions.classToAttributeName(classOf[ThreadLocal[_]]))
    assertEquals("illegalStateException", Conventions.classToAttributeName(classOf[IllegalStateException]))
  }
  
  @Test
  def package_name_to_path() {
    assertEquals("/", Conventions.packageNameToPath(""))
    assertEquals("blog/", Conventions.packageNameToPath("blog"))
    assertEquals("blog/comments/", Conventions.packageNameToPath("blog.comments"))
  }
  
  @Test
  def package_name_to_path_excluding_roots() {
    assertEquals("blog/", Conventions.packageNameToPath(Nil, "blog"))
    assertEquals("blog/", Conventions.packageNameToPath(List("com.example"), "com.example.blog"))
    assertEquals("blog/", Conventions.packageNameToPath(List("com.example"), "blog"))
  }
}