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

import com.tzavellas.coeus.bind.Error

class CreditCardConstraint(acceptedCardTypes: CreditCardType*) extends Constraint[String] {
  import CreditCardType._
  
  def this() {
    this(CreditCardType.Visa,
         CreditCardType.Amex,
         CreditCardType.Discover,
         CreditCardType.Mastercard,
         CreditCardType.DinersClub)
  }
  
  def isValid(card: String): Boolean = {
    card == null || (hasValidNumber(card) && isOfAcceptedType(card))
  }
  
  def getError(targetClass: Class[_], field: String, value: String) = 
    Error.validationFailure("creditCard.invalid", field, targetClass, value)
  
  private def hasValidNumber(card: String) =
    (13 to 19 contains card.length) && luhnCheck(card)
    
  private def isOfAcceptedType(card: String) =
    acceptedCardTypes.exists(_.matches(card))
  
  /*
   * Checks whether a string of digits is a valid credit card number according
   * to the Luhn algorithm.
   *
   * 1. Starting with the second to last digit and moving left, double the
   *    value of all the alternating digits. For any digits that thus become
   *    10 or more, add their digits together. For example, 1111 becomes 2121,
   *    while 8763 becomes 7733 (from (1+6)7(1+2)3).
   *
   * 2. Add all these digits together. For example, 1111 becomes 2121, then
   *    2+1+2+1 is 6; while 8763 becomes 7733, then 7+7+3+3 is 20.
   *
   * 3. If the total ends in 0 (put another way, if the total modulus 10 is
   *    0), then the number is valid according to the Luhn formula, else it is
   *    not valid. So, 1111 is not valid (as shown above, it comes out to 6),
   *    while 8763 is valid (as shown above, it comes out to 20).
   *
   * @param cardNumber the credit card number to validate.
   * @return true if the number is valid, false otherwise.
   */
  private def luhnCheck(cardNumber: String): Boolean = {
    // number must be validated as 0..9 numeric first!!
    val digits = cardNumber.length()
    val oddOrEven = digits & 1
    var sum = 0
    for (count <- 0 until digits) {
    var digit = 0
    try {
      digit = Integer.parseInt(cardNumber.charAt(count) + "")
    } catch {
      case e: NumberFormatException => return false
    }
      if (((count & 1) ^ oddOrEven) == 0) { // not
        digit *= 2
      if (digit > 9) { digit -= 9 }
      }
      sum += digit
    }
    return if (sum == 0) false else (sum % 10 == 0)
  }
}

/**
 * CreditCardType implementations define how validation is performed
 * for one type/brand of credit card.
 */
trait CreditCardType {
  def matches(card: String): Boolean
}

object CreditCardType {

  object Visa extends CreditCardType {
    def matches(card: String): Boolean = 
      card(0) == '4' && (card.length == 13 || card.length == 16)
  }

  object Amex extends CreditCardType {
    def matches(card: String): Boolean = card.substring(0, 2) match {
    case "34" | "37" => card.length == 15
    case _ => false
    }
  }

  object Discover extends CreditCardType {
    def matches(card: String): Boolean =
      card.substring(0, 4) == "6011" && card.length == 16
  }

  object Mastercard extends CreditCardType {
    def matches(card: String): Boolean = card.substring(0, 2) match {
    case "51" | "52" | "53" | "54" | "55" => card.length == 16
    case _ => false
    }   
  }

  object DinersClub extends CreditCardType {
    def matches(card: String): Boolean = card.substring(0, 3) match {
    case "300" | "301" | "302" | "303" | "304" | "305" => card.length == 14
    case _ => false
    }
  }

  object SkipCartTypeValidation extends CreditCardType {
    def matches(card: String): Boolean = true
  }
}