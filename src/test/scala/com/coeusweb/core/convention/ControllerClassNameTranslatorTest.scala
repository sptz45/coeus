/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.convention

import org.junit.Test
import org.junit.Assert._

class ControllerClassToUrlPrefixTest {

  import test.controllers._
  
  val basePackages = List(classOf[PostController].getPackage.getName)
  
  @Test
  def default_translator() {
    val default = new DefaultControllerClassNameTranslator(basePackages)
    assertEquals("post", default.translate(classOf[PostController]))
    assertEquals("users/users", default.translate(classOf[users.Users]))
    assertEquals("users/email", default.translate(classOf[users.EmailController]))
    assertEquals("users/auth/basicAuthentication", default.translate(classOf[users.auth.BasicAuthenticationController]))  
  }
  
  @Test
  def default_translator_without_base_package() {
    val default = new DefaultControllerClassNameTranslator(Nil)
    assertEquals("post", default.translate(classOf[PostController]))
    assertEquals("users", default.translate(classOf[users.Users]))
    assertEquals("email", default.translate(classOf[users.EmailController]))
    assertEquals("basicAuthentication", default.translate(classOf[users.auth.BasicAuthenticationController]))  
  }
  
  @Test
  def dashed_translator() {
    val dashed = new DashedControllerClassNameTranslator(basePackages)
    assertEquals("post", dashed.translate(classOf[PostController]))
    assertEquals("users/users", dashed.translate(classOf[users.Users]))
    assertEquals("users/email", dashed.translate(classOf[users.EmailController]))
    assertEquals("users/auth/basic-authentication", dashed.translate(classOf[users.auth.BasicAuthenticationController]))
  }
  
  @Test
  def dashed_translator_without_base_package() {
    val dashed = new DashedControllerClassNameTranslator(Nil)
    assertEquals("post", dashed.translate(classOf[PostController]))
    assertEquals("users", dashed.translate(classOf[users.Users]))
    assertEquals("email", dashed.translate(classOf[users.EmailController]))
    assertEquals("basic-authentication", dashed.translate(classOf[users.auth.BasicAuthenticationController]))
  }
}

package test.controllers {
  class PostController
  package users {
    class Users
    class EmailController
    package auth {
      class BasicAuthenticationController
    }
  }
}