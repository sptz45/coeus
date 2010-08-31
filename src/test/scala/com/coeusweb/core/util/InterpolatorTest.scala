/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.util

import org.junit.Test
import org.junit.Assert._

class InterpolatorTest {
  import Interpolator._

  val args: Seq[String] = Array("email", "User", "null")
  
  @Test
  def string_with_no_internpolation_variables() {
    assertEquals("is required", interpolateNumericVars("is required", Nil))
    assertEquals("is required", interpolateVars("is required", Nil))
  }
  
  @Test
  def string_with_numeric_internpolation_variables() {
    assertEquals("email in User cannot be null", interpolateNumericVars("{1} in {2} cannot be {3}", args))
    assertEquals("emailUsernull", interpolateNumericVars("{1}{2}{3}", args))
    assertEquals(" null User email ", interpolateNumericVars(" {3} {2} {1} ", args))
    assertEquals("error:email is null", interpolateNumericVars("error:{1} is {3}", args))
  }
  
  @Test
  def simple_interpolation() {
    assertEquals("/owner/12/", interpolateVars("/owner/{ownerId}/", Seq(12)))
    assertEquals("/owner/12/pet/3", interpolateVars("/owner/{ownerId}/pet/{petId}", Seq(12, 3)))
  }
  
  @Test
  def escape_interpolation_variables() {
    assertEquals("email in User cannot be {null}", interpolateNumericVars("{1} in {2} cannot be \\{null}", args))
    assertEquals("email in User cannot be {null}", interpolateVars("{1} in {2} cannot be \\{null}", args))
  }
}