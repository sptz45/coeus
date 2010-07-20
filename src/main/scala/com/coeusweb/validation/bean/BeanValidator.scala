/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.bean

import javax.validation.{ Validator => Jsr303Validator }
import com.coeusweb.bind.{ BindingResult, Error }
import com.coeusweb.validation.Validator

/**
 * Adapts a JSR303 validator to the Coeus Validator interface.
 * 
 * @param validator the JSR303 validator that will be used for validating the objects
 */
class BeanValidator[-T <: AnyRef](val validator: Jsr303Validator) extends Validator[T] {

  def validate(target: T): Iterable[Error] = new Iterable[Error] {
    def iterator = new BeanValidatorValidator.ErrorIterator(validator.validate(target).iterator)
  }
  
  def validate(result: BindingResult[T]) {
    val violations = validator.validate(result.target).iterator
    while (violations.hasNext) {
      val violation = violations.next
      result.addError(violation.getPropertyPath.toString, Error(violation.getMessage))
    }
  }
  
  def validateField[S](field: String, value: Any)(implicit m: Manifest[S]): Option[Error] = {
    val violations = validator.validateValue(m.erasure, field, value)
    if (violations.isEmpty) None
    else Some(Error(violations.iterator.next.getMessage))
  }
}


private object BeanValidatorValidator {  
  import java.util.{ Iterator => JIterator }
  import javax.validation.ConstraintViolation
  
  class ErrorIterator[T](violations: JIterator[ConstraintViolation[T]]) extends Iterator[Error] {
    
    def hasNext = violations.hasNext
    
    def next = Error(violations.next.getMessage)
  }
}