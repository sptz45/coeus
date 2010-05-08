/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec.constraint

import com.coeusweb.bind.Error

trait NumericConstraints {

  // -- BigDecimal -----------------------------------------------------

  def isGreaterThan(min: BigDecimal) = new Constraint[BigDecimal] {
    def isValid(value: BigDecimal) = value == null || value > min
    
    def getError(targetClass: Class[_], field: String, value: BigDecimal) = 
      new Error("decimal.min", field, targetClass, value, min)
  }

  def isLessThan(max: BigDecimal) = new Constraint[BigDecimal] {
    def isValid(value: BigDecimal) = value == null || value < max
    
    def getError(targetClass: Class[_], field: String, value: BigDecimal) = 
      new Error("decimal.max", field, targetClass, value, max)
  }

  def isBetween(min: BigDecimal, max: BigDecimal) = new Constraint[BigDecimal] {
    def isValid(value: BigDecimal) = value == null || (min <= value && value < max)
    
    def getError(targetClass: Class[_], field: String, value: BigDecimal) = 
      Error.validationFailure("decimal.between", field, targetClass, value, min, max)
  }

  // -- Short ----------------------------------------------------------

  def isGreaterThan(min: Short) = new Constraint[Short] {
    def isValid(value: Short) = value > min
    
    def getError(targetClass: Class[_], field: String, value: Short) = 
      Error.validationFailure("short.min", field, targetClass, value, min)
  }

  def isLessThan(max: Short) = new Constraint[Short] {
    def isValid(value: Short) = value < max
    
    def getError(targetClass: Class[_], field: String, value: Short) = 
      Error.validationFailure("short.max", field, targetClass, value, max)
  }

  def isBetween(min: Short, max: Short) = new Constraint[Short] {
    def isValid(value: Short) = (min <= value) && (value < max)
    
    def getError(targetClass: Class[_], field: String, value: Short) = 
      Error.validationFailure("short.between", field, targetClass, value, min, max)
  }

  // -- Int ------------------------------------------------------------

  def isGreaterThan(min: Int) = new Constraint[Int] {
    def isValid(value: Int) = value > min
    
    def getError(targetClass: Class[_], field: String, value: Int) = 
      Error.validationFailure("integer.min", field, targetClass, value, min)
  }

  def isLessThan(max: Int) = new Constraint[Int] {
    def isValid(value: Int) = value < max
    
    def getError(targetClass: Class[_], field: String, value: Int) = 
      Error.validationFailure("integer.max", field, targetClass, value, max)
  }

  def isBetween(min: Int, max: Int) = new Constraint[Int] {
    def isValid(value: Int) = (min <= value) && (value < max)
    
    def getError(targetClass: Class[_], field: String, value: Int) = 
      Error.validationFailure("integer.between", field, targetClass, value, min, max)
  }

  def isBetween(range: Range) = new Constraint[Int] {
    def isValid(value: Int) = range.contains(value)
    
    def getError(targetClass: Class[_], field: String, value: Int) = 
      Error.validationFailure("integer.range", field, targetClass, value, range.head, range.last)
  }

  // -- Long -----------------------------------------------------------

  def isGreaterThan(min: Long) = new Constraint[Long] {
    def isValid(value: Long) = value > min
    
    def getError(targetClass: Class[_], field: String, value: Long) = 
      Error.validationFailure("long.min", field, targetClass, value, min)
  }

  def isLessThan(max: Long) = new Constraint[Long] {
    def isValid(value: Long) = value < max
    
    def getError(targetClass: Class[_], field: String, value: Long) = 
      Error.validationFailure("long.max", field, targetClass, value, max)
  }

  def isBetween(min: Long, max: Long) = new Constraint[Long] {
    def isValid(value: Long) = (min <= value) && (value < max)
    
    def getError(targetClass: Class[_], field: String, value: Long) = 
      Error.validationFailure("long.between", field, targetClass, value, min, max)
  }

  // -- Float ----------------------------------------------------------

  def isGreaterThan(min: Float) = new Constraint[Float] {
    def isValid(value: Float) = value > min
    
    def getError(targetClass: Class[_], field: String, value: Float) = 
      Error.validationFailure("float.min", field, targetClass, value, min)
  }

  def isLessThan(max: Float) = new Constraint[Float] {
    def isValid(value: Float) = value < max
    
    def getError(targetClass: Class[_], field: String, value: Float) = 
      Error.validationFailure("float.max", field, targetClass, value, max)
  }

  def isBetween(min: Float, max: Float) = new Constraint[Float] {
    def isValid(value: Float) = (min <= value) && (value < max)
    
    def getError(targetClass: Class[_], field: String, value: Float) = 
      Error.validationFailure("float.between", field, targetClass, value, min, max)
  }

  // -- Double ---------------------------------------------------------

  def isGreaterThan(min: Double) = new Constraint[Double] {
    def isValid(value: Double) = value > min
    
    def getError(targetClass: Class[_], field: String, value: Double) = 
      Error.validationFailure("double.min", field, targetClass, value, min)
  }

  def isLessThan(max: Double) = new Constraint[Double] {
    def isValid(value: Double) = value < max
    
    def getError(targetClass: Class[_], field: String, value: Double) = 
      Error.validationFailure("double.max", field, targetClass, value, max)
  }

  def isBetween(min: Double, max: Double) = new Constraint[Double] {
    def isValid(value: Double) = (min <= value) && (value < max)
    
    def getError(targetClass: Class[_], field: String, value: Double) = 
      Error.validationFailure("double.between", field, targetClass, value, min, max)
  }
}
