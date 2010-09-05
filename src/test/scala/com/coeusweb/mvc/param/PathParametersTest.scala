/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.param

import java.util.Locale
import scala.collection.mutable.Map
import com.coeusweb.bind.ConverterRegistry
import com.coeusweb.test.servlet.MockHttpServletRequest

class PathParametersTest extends AbstractParametersTest {

  val pathVariables = Map[String, String]()
  val params = new PathParameters(null, Locale.US, ConverterRegistry.defaultConverters , pathVariables)
  
  def setParameter(name: String, value: String) {
    pathVariables += (name -> value)
  }
  
  @org.junit.Test
  def needed_for_eclipse_since_concrete_class_has_no_tests() { } 
}

