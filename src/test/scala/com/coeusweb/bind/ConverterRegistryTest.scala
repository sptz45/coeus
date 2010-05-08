/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind

import org.junit.Test
import org.junit.Assert._

class ConverterRegistryTest {

  val registry = DefaultConverterRegistry
  
  @Test
  def retrieve_a_converter() {
    assertEquals(1, registry.converter(classOf[Int]).parse("1", null))
    assertEquals(1, registry.converter(classOf[java.lang.Integer]).parse("1", null))
  }
  
  @Test(expected=classOf[NoConverterAvailableException])
  def exception_when_no_converter_available() {
    registry.converter(classOf[Thread])
  }
}
