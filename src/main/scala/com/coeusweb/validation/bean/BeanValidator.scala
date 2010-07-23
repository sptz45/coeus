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
 * Adapts a JSR303 validator to the Coeus Validator trait.
 * 
 * <p>Because {@code com.coeusweb.validation.Validator} is contravariant and because
 * {@code javax.validation.Validator} is not generic, we can validate all objects
 * (that have JSR303 annotations) using a single {@code BeanValidator} of type
 * {@code BeanValidator[AnyRef]}.</p>
 * 
 * <p>For example if we use {@link com.coeusweb.controller.FormProcessing} (that
 * requires the presence of an implicit {@code Validator}) we can define a single
 * abstract controller that has an <em>implicit</em> validator of type
 * {@code BeanValidator[AnyRef]} for all of our controllers that handle forms.</p>
 * 
 * <pre>
 * object DefaultValidator
 *   extends BeanValidator[AnyRef](
 *     Validation.buildDefaultValidatorFactory.getValidator)
 * 
 * abstract class FormController extends FormProcessing {
 *   implicit val validator: Validator[AnyRef] = DefaultValidator
 * }
 * </pre>
 * 
 * @param validator the JSR303 validator that will be used for validating the objects
 * @see BeanErrorFormatter
 */
class BeanValidator[-T <: AnyRef](val validator: Jsr303Validator) extends Validator[T] {

  def validate(target: T): Iterable[Error] = new Iterable[Error] {
    def iterator = new BeanValidator.ErrorIterator(validator.validate(target).iterator)
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


private object BeanValidator {
  import java.util.{ Iterator => JIterator }
  import javax.validation.ConstraintViolation
  
  class ErrorIterator[T](violations: JIterator[ConstraintViolation[T]]) extends Iterator[Error] {
    
    def hasNext = violations.hasNext
    
    def next = Error(violations.next.getMessage)
  }
}
