/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec.constraint

import com.coeusweb.bind.Error

trait GenericConstraints {
  
  def isNotNull[T <: AnyRef] = new Constraint[T] {
    def isValid(value: T) = value != null
    
    def getError(targetClass: Class[_], field: String, value: T) = 
      Error.validationFailure("notNull", field, targetClass, value)
  }
  
  def isOneOf[T](values: T*) = new Constraint[T] {
    def isValid(value: T) = values.contains(value)
    
    def getError(targetClass: Class[_], field: String, value: T) = 
      Error.validationFailure("enumerated", field, targetClass, value, values)
  }
  
  def satisfies[T](test: T => Boolean) = new Constraint[T] {
    def isValid(value: T) = value == null || test(value)
    
    def getError(targetClass: Class[_], field: String, value: T) = 
      Error.validationFailure("constraint", field, targetClass, value)
  }
  
  def isUnique[T](query: T => Option[_]) = new Constraint[T] {
    def isValid(value: T) = value == null || query(value) == None
    
    def getError(targetClass: Class[_], field: String, value: T) = 
      Error.validationFailure("unique", field, targetClass, value)
  }
}