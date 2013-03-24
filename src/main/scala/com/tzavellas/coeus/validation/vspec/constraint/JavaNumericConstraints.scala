/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.validation.vspec.constraint

import java.math.BigDecimal
import com.tzavellas.coeus.bind.Error

trait JavaNumericConstraints {
  
  def isGreaterThan(min: BigDecimal) = new Constraint[BigDecimal] {
    def isValid(value: BigDecimal) = value == null || value.compareTo(min) == 1
    
    def getError(targetClass: Class[_], field: String, value: BigDecimal) = 
      Error.validationFailure("java.decimal.min", field, targetClass, value, min)
  }
  
  def isLessThan(max: BigDecimal) = new Constraint[BigDecimal] {
    def isValid(value: BigDecimal) = value == null || value.compareTo(max) == -1
    
    def getError(targetClass: Class[_], field: String, value: BigDecimal) = 
      Error.validationFailure("java.decimal.max", field, targetClass, value, max)
  }
  
  def isBetween(min: BigDecimal, max: BigDecimal) = new Constraint[BigDecimal] {
    def isValid(value: BigDecimal) =
      value == null || (value.compareTo(min) != -1 && value.compareTo(max) == -1)
  
    def getError(targetClass: Class[_], field: String, value: BigDecimal) = 
      Error.validationFailure("java.decimal.between", field, targetClass, value, min, max)
  }
}
 