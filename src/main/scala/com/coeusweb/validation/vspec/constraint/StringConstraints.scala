/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec.constraint

import com.coeusweb.bind.Error

trait StringConstraints {
  
  def hasText = new Constraint[String] {
    def isValid(value: String) = value != null && !value.trim.isEmpty
    
    def getError(targetClass: Class[_], field: String, value: String) = 
      Error.validationFailure("hasText", field, targetClass, value)
  }
  
  def matches(pattern: String) = new Constraint[String] {
    private val compiled = java.util.regex.Pattern.compile(pattern)
    def isValid(value: String) = value == null || compiled.matcher(value).matches
    
    def getError(targetClass: Class[_], field: String, value: String) = 
      Error.validationFailure("pattern", field, targetClass, value, pattern)
  }
  
  def minLength(length: Int) = new Constraint[String] {
    def isValid(value: String) =
      if (value eq null) length == 0 else value.length >= length
   
    def getError(targetClass: Class[_], field: String, value: String) = 
      Error.validationFailure("string.length.min", field, targetClass, value, length)
  }
  
  def maxLength(length: Int) = new Constraint[String] {
    def isValid(value: String) =
      if (value eq null) true else value.length <= length
    
    def getError(targetClass: Class[_], field: String, value: String) = 
      Error.validationFailure("string.length.max", field, targetClass, value, length)
  }
  
  def isCreditCard(cartTypes: CreditCardType*) = new CreditCardConstraint(cartTypes: _*)
  
  def isEmail = new EmailConstraint
  
  def isInetAddress = new InternetAddressConstraint
  
  def isIsbn = new IsbnConstraint
  
  def isUrl(schemes: String*) = new UrlConstraint(schemes: _*)
}

