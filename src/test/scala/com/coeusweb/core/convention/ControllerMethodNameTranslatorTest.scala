/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.convention

import org.junit.Test
import org.junit.Assert._

class ControllerMethodNameTranslatorTest {

  var translator: ControllerMethodNameTranslator = _
  
  @Test
  def default_translator() {
    translator = new DefaultControllerMethodNameTranslator
    assertEquals("method", translate("method"))
    assertEquals("camelCase", translate("camelCase"))
    assertEquals("with_underscore", translate("with_underscore"))
  }
  
  @Test
  def dashed_translator() {
    translator = new DashedControllerMethodNameTranslator
    assertEquals("method", translate("method"))
    assertEquals("camel-case", translate("camelCase"))
    assertEquals("with_underscore", translate("with_underscore"))
  }
  
  def translate(methodName: String) = translator.translate(classOf[SampleClass].getMethod(methodName))
  
  abstract class SampleClass {
    def method
    def camelCase
    def with_underscore
  }
}