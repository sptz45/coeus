/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.util

import org.junit.Test
import org.junit.Assert._

class NumericVariableInterpolatorTest {
  import NumericVariableInterpolator.interpolate

  val args: Seq[String] = Array("email", "User", "null")
  
  @Test
  def string_with_no_internpolation_variables() {
    assertEquals("is required", interpolate("is required", Nil))
  }
  
  @Test
  def string_with_internpolation_variables() {
    assertEquals("email in User cannot be null", interpolate("{0} in {1} cannot be {2}", args))
    assertEquals("emailUsernull", interpolate("{0}{1}{2}", args))
    assertEquals(" null User email ", interpolate(" {2} {1} {0} ", args))
    assertEquals("error:email is null", interpolate("error:{0} is {2}", args))
  }
  
  @Test
  def escape_interpolation_variables() {
    assertEquals("email in User cannot be {null}", interpolate("{0} in {1} cannot be \\{null}", args))
  }
}