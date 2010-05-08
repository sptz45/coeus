/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec.constraint

import org.junit.Test

class GenericConstraintsTest {
  
  object constraints extends GenericConstraints

  @Test
  def valid_values_must_belong_in_the_enumeration() {
    val oneOrTwo = constraints.isOneOf("one", "two")
    assert(! oneOrTwo.isValid(null))
    assert(oneOrTwo.isValid("one"))
    assert(oneOrTwo.isValid("two"))
    assert(! oneOrTwo.isValid("three"))
  }
}