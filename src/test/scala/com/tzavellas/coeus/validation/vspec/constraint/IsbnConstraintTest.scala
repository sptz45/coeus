/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 * 
 * Ported from Apache Jakarta Commons Validator,
 * http://commons.apache.org/validator/
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.validation.vspec.constraint

import org.junit.Test

class IsbnConstraintTest {
  import ConstraintAssertions._
  import IsbnConstraintTest._
  
  val constraint = new IsbnConstraint
  
  @Test
  def invalid_isbn_numbers() {
    assertInvalid(constraint,
      "", "1", "12345678901234","dsasdsadsads",
      "535365", "I love sparrows!", "--1 930110 99 5",
      "1 930110 99 5--", "1 930110-99 5-", INVALID_ISBN)
  }

  @Test
  def valid_isbn_numbers() {
    assertValid(constraint,
      null, VALID_ISBN_RAW, VALID_ISBN_DASHES,
      VALID_ISBN_SPACES, VALID_ISBN_X, VALID_ISBN_x)
  }
}

private object IsbnConstraintTest {
    val VALID_ISBN_RAW = "1930110995"
    val VALID_ISBN_DASHES = "1-930110-99-5"
    val VALID_ISBN_SPACES = "1 930110 99 5"
    val VALID_ISBN_X = "0-201-63385-X"
    val VALID_ISBN_x = "0-201-63385-x"
    val INVALID_ISBN = "068-556-98-45"
}
