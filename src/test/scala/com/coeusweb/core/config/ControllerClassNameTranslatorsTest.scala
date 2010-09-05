/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.config

import org.junit.Test
import org.junit.Assert._

class ControllerClassNameTransaltorsTest {

  import test.controllers._
  
  val basePackages = List(classOf[PostController].getPackage.getName)
  var translator: Class[_] => String = _
  
  @Test
  def default_translator() {
    translator = ControllerConventions.useClassName(basePackages)
    assertEquals("post", translate[PostController])
    assertEquals("users/users", translate[users.Users])
    assertEquals("users/email", translate[users.EmailController])
    assertEquals("users/auth/basicAuthentication", translate[users.auth.BasicAuthenticationController])  
  }
  
  @Test
  def default_translator_without_base_package() {
    translator = ControllerConventions.useClassName()
    assertEquals("post", translate[PostController])
    assertEquals("users", translate[users.Users])
    assertEquals("email", translate[users.EmailController])
    assertEquals("basicAuthentication", translate[users.auth.BasicAuthenticationController])  
  }
  
  @Test
  def dashed_translator() {
    translator = ControllerConventions.useClassNameWithDashes(basePackages)
    assertEquals("post", translate[PostController])
    assertEquals("users/users", translate[users.Users])
    assertEquals("users/email", translate[users.EmailController])
    assertEquals("users/auth/basic-authentication", translate[users.auth.BasicAuthenticationController])
  }
  
  @Test
  def dashed_translator_without_base_package() {
    translator = ControllerConventions.useClassNameWithDashes()
    assertEquals("post", translate[PostController])
    assertEquals("users", translate[users.Users])
    assertEquals("email", translate[users.EmailController])
    assertEquals("basic-authentication", translate[users.auth.BasicAuthenticationController])
  }
  
  def translate[T](implicit m: Manifest[T]) = translator(m.erasure)
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