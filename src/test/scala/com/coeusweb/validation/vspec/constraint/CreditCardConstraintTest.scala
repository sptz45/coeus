/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 * 
 * Ported from Apache Jakarta Commons Validator,
 * http://commons.apache.org/validator/
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec.constraint

import org.junit.Test
import org.junit.Assert._

class CreditCardConstraintTest {
  import CreditCardConstraintTest._
  import ConstraintAssertions._
  
  val ccc = new CreditCardConstraint
  
  @Test
  def test_using_invalid_credit_cards() {
    assertInvalid(ccc, "", "123456789012", "12345678901234567890", "4417123456789112", "4417q23456w89113")
  }
  
  @Test
  def test_using_valid_credit_cards() {
    assertValid(ccc, null, VALID_VISA, VALID_SHORT_VISA, VALID_SHORT_VISA,
        VALID_MASTERCARD, VALID_DISCOVER, VALID_DINERS)
  }
  
  @Test
  def change_allowed_card_types() {
    val dinersOnly = new CreditCardConstraint(CreditCardType.DinersClub)
    assertValid(dinersOnly, VALID_DINERS)
    // Turned off all other card types so even valid numbers should fail
    assertInvalid(dinersOnly, VALID_VISA, VALID_AMEX, VALID_MASTERCARD, VALID_DISCOVER)
  }
  
  @Test
  def skip_cart_type_validation() {
    val noTypeValidation = new CreditCardConstraint(CreditCardType.SkipCartTypeValidation)
    assertValid(noTypeValidation, VALID_AMEX)
  }
}

private object CreditCardConstraintTest {
  val VALID_VISA = "4417123456789113"
  val VALID_SHORT_VISA = "4222222222222"
  val VALID_AMEX = "378282246310005"
  val VALID_MASTERCARD = "5105105105105100"
  val VALID_DISCOVER = "6011000990139424"
  val VALID_DINERS = "30569309025904"
}
