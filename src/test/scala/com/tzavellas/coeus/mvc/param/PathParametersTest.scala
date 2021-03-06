/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc.param

import java.util.Locale
import scala.collection.mutable.Map
import com.tzavellas.coeus.bind.ConverterRegistry

class PathParametersTest extends AbstractParametersTest {

  val pathVariables = Map[String, String]()
  val params = new PathParameters(pathVariables, Locale.US, ConverterRegistry.defaultConverters)
  
  def setParameter(name: String, value: String) {
    pathVariables += (name -> value)
  }
  
  @org.junit.Test
  def needed_for_eclipse_since_concrete_class_has_no_tests() { } 
}

