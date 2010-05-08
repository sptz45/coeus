/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec.constraint

import java.util.Date
import com.coeusweb.bind.Error

trait DateConstraints {
  
  def isBefore(date: Date) = new Constraint[Date] {
    
    def isValid(value: Date) = value == null || value.before(date)
    
    def getError(targetClass: Class[_], field: String, value: Date) = 
      Error.validationFailure("date.before", field, targetClass, value, date)
  }
  
  def isAfter(date: Date) = new Constraint[Date] {
    
    def isValid(value: Date) = value == null || value.after(date)
    
    def getError(targetClass: Class[_], field: String, value: Date) = 
      Error.validationFailure("date.after", field, targetClass, value, date)
  }
  
  def isInThePast = new Constraint[Date] {
    
    def isValid(value: Date) = value == null || value.before(new Date)
    
    def getError(targetClass: Class[_], field: String, value: Date) = 
      Error.validationFailure("date.past", field, targetClass, value)
  }
  
  def isInTheFuture = new Constraint[Date] {
    
    def isValid(value: Date) = value == null || value.after(new Date)
    
    def getError(targetClass: Class[_], field: String, value: Date)= 
      Error.validationFailure("date.future", field, targetClass, value)
  }
}
