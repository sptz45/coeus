/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.bean

import language.existentials

import javax.validation.{ Validator => JValidator }
import com.coeusweb.bind.{ BindingResult, Error }
import com.coeusweb.validation.Validator

/**
 * Adapts a JSR303 validator to the Coeus Validator trait.
 * 
 * <p>Because {@code com.coeusweb.validation.Validator} is contravariant and
 * because {@code javax.validation.Validator} is not generic, we can validate
 * all objects (that have JSR303 annotations) using a single {@code BeanValidator}
 * of type {@code BeanValidator[AnyRef]}.</p>
 * 
 * <p>For example if we use {@link com.coeusweb.controller.FormProcessing}
 * (that requires the presence of an implicit {@code Validator}) we can define
 * a single abstract controller that has an <em>implicit</em> validator of type
 * {@code BeanValidator[AnyRef]} for all of our controllers that handle forms.
 * </p>
 * 
 * <pre>
 * object DefaultValidator
 *   extends BeanValidator[AnyRef](
 *     Validation.buildDefaultValidatorFactory.getValidator)
 * 
 * abstract class FormController extends Controller with FormProcessing {
 *   implicit val validator: Validator[AnyRef] = DefaultValidator
 * }
 * </pre>
 * 
 * @param validator the JSR-303 validator that will be used for validating the
 *                  objects.
 * 
 * @see BeanErrorFormatter
 * @see Validation
 */
class BeanValidator[-T <: AnyRef](val validator: JValidator) extends Validator[T] {
  
  val errorFormatter = BeanErrorFormatter

  def validate(target: T): Iterable[Error] = new Iterable[Error] {
    def iterator = new BeanValidator.ErrorIterator(validator.validate(target).iterator)
  }
  
  def validate(result: BindingResult[T]) {
    result.errorFormatter = errorFormatter
    val violations = validator.validate(result.target).iterator
    while (violations.hasNext) {
      val violation = violations.next
      result.addError(violation.getPropertyPath.toString, Error(violation.getMessage))
    }
  }
  
  def validateField[F](field: String, value: Any)(implicit m: Manifest[F]) = {
    val violations = validator.validateValue(m.runtimeClass, field, value)
    if (violations.isEmpty) None
    else Some(Error(violations.iterator.next.getMessage))
  }
}


object BeanValidator {
  
  import java.util.Locale
  
  /**
   * Create a validator using the default JSR-303 provider with the default
   * configuration and {@code LocaleAwareInterpolator}. 
   * 
   * @param offlineLocale the offline locale of {@code LocaleAwareInterpolator}.
   * 
   * @return a {@code BeanValidator} with the default configuration.
   */
  def defaultValidator(offlineLocale: Locale): Validator[AnyRef] = {
    new BeanValidator(Validation.defaultConfig(offlineLocale)
                                .buildValidatorFactory()
                                .getValidator())
  }
  
  import java.util.{ Iterator => JIterator }
  import javax.validation.ConstraintViolation
  
  private class ErrorIterator[T](violations: JIterator[ConstraintViolation[T]])
    extends Iterator[Error] {
    
    def hasNext = violations.hasNext
    
    def next = Error(violations.next.getMessage)
  }
}
