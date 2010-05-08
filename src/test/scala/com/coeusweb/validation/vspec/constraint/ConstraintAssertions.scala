/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec.constraint

import org.junit.Assert._

object ConstraintAssertions {
  
  def assertValid[T](constraint: Constraint[T] , values: T*) {
    for (value <- values)
      assertTrue("[" + value + "] must be valid", constraint.isValid(value))
  }
  
  def assertInvalid[T](constraint: Constraint[T], values: T*) {
    for (value <- values)
      assertFalse("[" + value + "] must be invalid", constraint.isValid(value))
  }
}
