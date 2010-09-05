/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.param

import java.util.Locale
import com.coeusweb.bind.ConverterRegistry
import org.springframework.mock.web.MockHttpServletRequest

class RequestParametersTest extends AbstractParametersTest {
  
  val mock = new MockHttpServletRequest
  val params = new RequestParameters(mock, Locale.US, ConverterRegistry.defaultConverters )
  
  
  def setParameter(name: String, value: String) {
    mock.setParameter(name, value)
  }
  
  @org.junit.Test
  def needed_for_eclipse_since_concrete_class_has_no_tests() { }
}
