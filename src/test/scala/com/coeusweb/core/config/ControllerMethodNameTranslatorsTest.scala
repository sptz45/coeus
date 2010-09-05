/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.config

import java.lang.reflect.Method
import org.junit.Test
import org.junit.Assert._

class ControllerMethodNameTranslatorsTest {

  var translator: Method => String = _
  
  @Test
  def default_translator() {
    translator = ControllerConventions.useMethodName
    assertEquals("method", translate("method"))
    assertEquals("camelCase", translate("camelCase"))
    assertEquals("with_underscore", translate("with_underscore"))
  }
  
  @Test
  def dashed_translator() {
    translator = ControllerConventions.useMethodNameWithDashes
    assertEquals("method", translate("method"))
    assertEquals("camel-case", translate("camelCase"))
    assertEquals("with_underscore", translate("with_underscore"))
  }
  
  def translate(methodName: String) = translator(classOf[SampleClass].getMethod(methodName))
  
  abstract class SampleClass {
    def method
    def camelCase
    def with_underscore
  }
}