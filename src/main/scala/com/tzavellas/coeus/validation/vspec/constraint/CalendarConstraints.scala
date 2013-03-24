/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.validation.vspec.constraint

import com.tzavellas.coeus.bind.Error
import java.util.Calendar

trait CalendarConstraints {
  
  def isBefore(cal: Calendar) = new Constraint[Calendar] {
    
    def isValid(value: Calendar) = value == null || value.before(cal)
    
    def getError(targetClass: Class[_], field: String, value: Calendar)= 
      Error.validationFailure("calendar.before", field, targetClass, value, cal)
  }
  
  def isAfter(cal: Calendar) = new Constraint[Calendar] {
    
    def isValid(value: Calendar) = value == null || value.after(cal)
    
    def getError(targetClass: Class[_], field: String, value: Calendar) = 
      Error.validationFailure("calendar.after", field, targetClass, value, cal)
  }
  
  def isCalInThePast = new Constraint[Calendar] {
    
    def isValid(value: Calendar) =
      value == null || value.before(Calendar.getInstance)
    
    def getError(targetClass: Class[_], field: String, value: Calendar) = 
      Error.validationFailure("calendar.past", field, targetClass, value)
  }
  
  def isCalInTheFuture = new Constraint[Calendar] {
    
    def isValid(value: Calendar) =
      value == null || value.after(Calendar.getInstance)
    
    def getError(targetClass: Class[_], field: String, value: Calendar) = 
      Error.validationFailure("calendar.future", field, targetClass, value)
  }
}
